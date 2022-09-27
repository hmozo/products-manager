package com.doctorkernel.ordersservice.interfaces.saga;

import com.doctorkernel.core.domain.commands.ReserveProductCommand;
import com.doctorkernel.core.domain.events.ProductReservedEvent;
import com.doctorkernel.core.domain.queries.FetchUserPaymentDetailsQuery;
import com.doctorkernel.ordersservice.domain.events.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

@Saga
@Slf4j
public class OrderSaga {
    @Autowired
    private transient CommandGateway commandGateway;

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

        //FetchUserPaymentDetailsQuery fetchUserPaymentDetailsQuery= new FetchUserPaymentDetailsQuery(productReservedEvent.getUserId());

    }



}
