package com.doctorkernel.ordersservice.interfaces.saga;

import com.doctorkernel.core.domain.commands.ProcessPaymentCommand;
import com.doctorkernel.core.domain.commands.ReserveProductCommand;
import com.doctorkernel.core.domain.events.PaymentProcessedEvent;
import com.doctorkernel.core.domain.events.ProductReservedEvent;
import com.doctorkernel.core.domain.model.User;
import com.doctorkernel.core.domain.queries.FetchUserPaymentDetailsQuery;
import com.doctorkernel.ordersservice.domain.events.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Saga
@Slf4j
public class OrderSaga {
    @Autowired
    private transient CommandGateway commandGateway;
    @Autowired
    private transient QueryGateway queryGateway;

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
            return;
        }
        log.info(("User fetched successfully: " + userPaymentDetails.getFirstName()));

        ProcessPaymentCommand processPaymentCommand= ProcessPaymentCommand.builder()
                .orderId(productReservedEvent.getOrderId())
                .paymentDetails(userPaymentDetails.getPaymentDetails())
                .paymentId(UUID.randomUUID().toString())
                .build();

        String result= null;
        try {
           result= commandGateway.sendAndWait(processPaymentCommand, 10, TimeUnit.SECONDS);
        }catch (Exception ex){
            log.error(ex.getMessage());
        }

        if (result==null){
            log.info("ProcessingPaymentCommand resulted NULL. Initiating compensating transaction");
        }
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(PaymentProcessedEvent paymentProcessedEvent){

    }



}
