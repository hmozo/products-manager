package com.doctorkernel.ordersservice.application.queryapi;

import com.doctorkernel.ordersservice.domain.OrderRepository;
import com.doctorkernel.ordersservice.domain.projection.OrderEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public void saveOrder(OrderEntity orderEntity){
        orderRepository.save(orderEntity);
    }

    public Optional<OrderEntity> findByOrderId(String orderId){
        return orderRepository.findByOrderId(orderId);
    }

    public void deleteByOrderId(String orderId){
        orderRepository.deleteByOrderId(orderId);
    }
}
