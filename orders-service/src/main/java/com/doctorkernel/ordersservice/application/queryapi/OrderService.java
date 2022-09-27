package com.doctorkernel.ordersservice.application.queryapi;

import com.doctorkernel.ordersservice.domain.OrderRepository;
import com.doctorkernel.ordersservice.domain.projection.OrderEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public void saveOrder(OrderEntity orderEntity){
        orderRepository.save(orderEntity);
    }
}
