package com.doctorkernel.ordersservice.interfaces.eventHandlers;

import com.doctorkernel.ordersservice.application.queryapi.OrderService;
import com.doctorkernel.ordersservice.domain.OrderRepository;
import com.doctorkernel.ordersservice.domain.events.OrderCreatedEvent;
import com.doctorkernel.ordersservice.domain.projection.OrderEntity;
import com.doctorkernel.ordersservice.infrastructure.repositories.jpa.OrderJPARepository;
import lombok.AllArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@ProcessingGroup("order-group")
public class OrderEventHandler {

    private final OrderService orderService;

    @EventHandler
    public void on(OrderCreatedEvent orderCreatedEvent) throws Exception{
        OrderEntity orderEntity= new OrderEntity();
        BeanUtils.copyProperties(orderCreatedEvent, orderEntity);

        orderService.saveOrder(orderEntity);
    }


}
