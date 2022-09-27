package com.doctorkernel.ordersservice.domain.model;

import com.doctorkernel.ordersservice.domain.commands.CreateOrderCommand;
import com.doctorkernel.ordersservice.domain.events.OrderCreatedEvent;
import com.doctorkernel.ordersservice.domain.util.OrderStatus;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
@NoArgsConstructor
public class OrderAggregate {
    @AggregateIdentifier
    public String orderId;
    private String userId;
    private String productId;
    private int quantity;
    private String addressId;
    private OrderStatus orderStatus;

    @CommandHandler
    public OrderAggregate(CreateOrderCommand createOrderCommand){
       OrderCreatedEvent orderCreatedEvent= new OrderCreatedEvent();
       BeanUtils.copyProperties(createOrderCommand, orderCreatedEvent);

       AggregateLifecycle.apply(orderCreatedEvent);
    }

    @EventSourcingHandler
    public void on(OrderCreatedEvent orderCreatedEvent){
        this.orderId= orderCreatedEvent.getOrderId();
        this.userId= orderCreatedEvent.getUserId();
        this.productId= orderCreatedEvent.getProductId();
        this.quantity= orderCreatedEvent.getQuantity();
        this.addressId= orderCreatedEvent.getAddressId();
        this.orderStatus= orderCreatedEvent.getOrderStatus();
    }

}
