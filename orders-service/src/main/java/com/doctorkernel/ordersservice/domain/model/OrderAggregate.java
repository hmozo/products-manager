package com.doctorkernel.ordersservice.domain.model;

import com.doctorkernel.ordersservice.domain.commands.ApproveOrderCommand;
import com.doctorkernel.ordersservice.domain.commands.CreateOrderCommand;
import com.doctorkernel.ordersservice.domain.commands.RejectOrderCommand;
import com.doctorkernel.ordersservice.domain.events.OrderApprovedEvent;
import com.doctorkernel.ordersservice.domain.events.OrderCreatedEvent;
import com.doctorkernel.ordersservice.domain.events.OrderRejectedEvent;
import com.doctorkernel.ordersservice.domain.util.OrderStatus;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
@NoArgsConstructor
@Slf4j
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

    @CommandHandler
    public void handle(ApproveOrderCommand approveOrderCommand){
        OrderApprovedEvent orderApprovedEvent= OrderApprovedEvent.builder()
                .orderId(approveOrderCommand.getOrderId()).build();
        AggregateLifecycle.apply(orderApprovedEvent);
    }

    @CommandHandler
    public void handle(RejectOrderCommand rejectOrderCommand){
        OrderRejectedEvent orderRejectedEvent= OrderRejectedEvent.builder()
                .orderId(rejectOrderCommand.getOrderId()).reason(rejectOrderCommand.getReason()).build();

        AggregateLifecycle.apply(orderRejectedEvent);
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

    @EventSourcingHandler
    public void on(OrderApprovedEvent orderApprovedEvent){
        this.orderStatus= orderApprovedEvent.getOrderStatus();
    }

    @EventSourcingHandler
    public void on(OrderRejectedEvent orderRejectedEvent){
        this.orderStatus= orderRejectedEvent.getOrderStatus();
    }
}
