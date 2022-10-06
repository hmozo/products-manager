package com.doctorkernel.ordersservice.interfaces.saga;

import com.doctorkernel.core.domain.commands.CancelProductReservationCommand;
import com.doctorkernel.core.domain.commands.ProcessPaymentCommand;
import com.doctorkernel.core.domain.commands.ReserveProductCommand;
import com.doctorkernel.core.domain.events.PaymentProcessedEvent;
import com.doctorkernel.core.domain.events.ProductReservationCancelledEvent;
import com.doctorkernel.core.domain.events.ProductReservedEvent;
import com.doctorkernel.core.domain.model.User;
import com.doctorkernel.core.domain.queries.FetchUserPaymentDetailsQuery;
import com.doctorkernel.ordersservice.domain.commands.ApproveOrderCommand;
import com.doctorkernel.ordersservice.domain.commands.RejectOrderCommand;
import com.doctorkernel.ordersservice.domain.events.OrderApprovedEvent;
import com.doctorkernel.ordersservice.domain.events.OrderCreatedEvent;
import com.doctorkernel.ordersservice.domain.events.OrderRejectedEvent;
import com.doctorkernel.ordersservice.domain.util.OrderSummary;
import com.doctorkernel.ordersservice.interfaces.eventHandlers.FindOrderQuery;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.deadline.annotation.DeadlineHandler;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Saga
@Slf4j
public class OrderSaga {
    @Autowired
    private transient CommandGateway commandGateway;
    @Autowired
    private transient QueryGateway queryGateway;
    @Autowired
    private transient DeadlineManager deadlineManager;

    @Autowired
    private transient QueryUpdateEmitter queryUpdateEmitter;

    private final String PAYMENT_PROCESSING_DEADLINE= "payment-processing-deadline";
    private String scheduleId;

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle (OrderCreatedEvent orderCreatedEvent){

        ReserveProductCommand reserveProductCommand= ReserveProductCommand.builder()
                .orderId(orderCreatedEvent.getOrderId())
                .productId(orderCreatedEvent.getProductId())
                .quantity(orderCreatedEvent.getQuantity())
                .userId(orderCreatedEvent.getUserId())
                .build();

        log.info("OrderCreatedEvent handled for orderId: " + orderCreatedEvent.getOrderId() + " and productId: " + orderCreatedEvent.getProductId());

        commandGateway.send(reserveProductCommand, (commandMessage, commandResultMessage)->{
            if (commandResultMessage.isExceptional()) {
                RejectOrderCommand rejectOrderCommand= RejectOrderCommand.builder()
                        .orderId(orderCreatedEvent.getOrderId())
                        .reason(commandResultMessage.optionalExceptionResult().get().getMessage()).build();

                commandGateway.send((rejectOrderCommand));
            }
        });
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductReservedEvent productReservedEvent){
        log.info("ProductReservedEvent called for productId" + productReservedEvent.getProductId() + " and orderId: " + productReservedEvent.getOrderId());

        FetchUserPaymentDetailsQuery fetchUserPaymentDetailsQuery= FetchUserPaymentDetailsQuery.builder()
                .userId(productReservedEvent.getUserId())
                .build();

        User userPaymentDetails= null;
        try{
            userPaymentDetails= queryGateway.query(fetchUserPaymentDetailsQuery, ResponseTypes.instanceOf(User.class)).join();
        }catch (Exception ex){
            log.error(ex.getMessage());
            cancelProductReservation(productReservedEvent, ex.getMessage());
            return;
        }

        if (userPaymentDetails==null){
            cancelProductReservation(productReservedEvent, "Could not fetch userPaymentDetails");
            return;
        }

        log.info(("User fetched successfully: " + userPaymentDetails.getFirstName()));

        scheduleId = deadlineManager.schedule(Duration.of(50, ChronoUnit.SECONDS),
                PAYMENT_PROCESSING_DEADLINE, productReservedEvent);

        ProcessPaymentCommand processPaymentCommand= ProcessPaymentCommand.builder()
                .orderId(productReservedEvent.getOrderId())
                .paymentDetails(userPaymentDetails.getPaymentDetails())
                .paymentId(UUID.randomUUID().toString())
                .build();

        String result= null;
        try {
           result= commandGateway.sendAndWait(processPaymentCommand);
        }catch (Exception ex){
            log.error(ex.getMessage());
            cancelProductReservation(productReservedEvent, ex.getMessage());
            return;
        }

        if (result==null){
            log.info("ProcessingPaymentCommand resulted NULL. Initiating compensating transaction");
            cancelProductReservation(productReservedEvent, "Could not process user payment");
        }
    }

    private void cancelProductReservation(ProductReservedEvent productReservedEvent, String reason){
        cancelDeadline();

        CancelProductReservationCommand cancelProductReservationCommand= CancelProductReservationCommand.builder()
                .orderId(productReservedEvent.getOrderId())
                .productId(productReservedEvent.getProductId())
                .quantity(productReservedEvent.getQuantity())
                .userId(productReservedEvent.getUserId())
                .reason(reason)
                .build();

        commandGateway.send(cancelProductReservationCommand);
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(PaymentProcessedEvent paymentProcessedEvent){
        cancelDeadline();

        log.info("PaymentProcessedEvent called with paymentId: " + paymentProcessedEvent.getPaymentId() + " and orderId: " + paymentProcessedEvent.getOrderId());
        ApproveOrderCommand approveOrderCommand= ApproveOrderCommand.builder().orderId(paymentProcessedEvent.getOrderId()).build();

        commandGateway.send(approveOrderCommand);
    }

    private void cancelDeadline(){
        if (scheduleId !=null) {
            deadlineManager.cancelSchedule(PAYMENT_PROCESSING_DEADLINE, scheduleId);
            scheduleId= null;
        }
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderApprovedEvent orderApprovedEvent){
        log.info("Order approved. Order SAGA completed for orderId: " + orderApprovedEvent.getOrderId());
        //SagaLifecycle.end();
        queryUpdateEmitter.emit(FindOrderQuery.class, query->true,
                OrderSummary.builder().orderId(orderApprovedEvent.getOrderId()).orderStatus(orderApprovedEvent.getOrderStatus()).build());
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductReservationCancelledEvent productReservationCancelledEvent){
        RejectOrderCommand rejectOrderCommand= RejectOrderCommand.builder()
                .orderId(productReservationCancelledEvent.getOrderId())
                .reason(productReservationCancelledEvent.getReason()).build();

        commandGateway.send((rejectOrderCommand));
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderRejectedEvent orderRejectedEvent){
        log.info("Successfully rejected order with id: " + orderRejectedEvent.getOrderId());
        queryUpdateEmitter.emit(FindOrderQuery.class, query->true,
                OrderSummary.builder()
                        .orderId(orderRejectedEvent.getOrderId())
                        .orderStatus(orderRejectedEvent.getOrderStatus())
                        .message(orderRejectedEvent.getReason()).build());
    }

    @DeadlineHandler(deadlineName = PAYMENT_PROCESSING_DEADLINE)
    public void handlePaymentDeadline(ProductReservedEvent productReservedEvent){
        log.info("Payment processing deadline took place. Sending a compensating commmand to cancel the product reservation");

        cancelProductReservation(productReservedEvent, "Payment timeout");
    }

}
