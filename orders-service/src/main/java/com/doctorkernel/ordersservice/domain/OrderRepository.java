package com.doctorkernel.ordersservice.domain;

import com.doctorkernel.ordersservice.domain.projection.OrderEntity;

import java.util.Optional;

public interface OrderRepository {
    OrderEntity save(OrderEntity orderEntity);
    Optional<OrderEntity> findByOrderId(String orderId);
    void deleteByOrderId(String orderId);
}
