package com.doctorkernel.ordersservice.interfaces.eventHandlers;

import com.doctorkernel.ordersservice.application.queryapi.OrderService;
import com.doctorkernel.ordersservice.domain.projection.OrderEntity;
import com.doctorkernel.ordersservice.domain.util.OrderSummary;
import lombok.AllArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class OrderQueriesHandler {

    private final OrderService orderService;

    @QueryHandler
    public OrderSummary findOrder(FindOrderQuery findOrderQuery){
        Optional<OrderEntity> orderEntityOpt= orderService.findByOrderId(findOrderQuery.getOrderId());
        if (orderEntityOpt.isEmpty()){
            throw new IllegalArgumentException("Order with orderId: " + findOrderQuery.getOrderId() + " doesn't exist");
        }
        return OrderSummary.builder()
                        .orderId(orderEntityOpt.get().getOrderId())
                        .orderStatus(orderEntityOpt.get().getOrderStatus()).build();
    }
}
