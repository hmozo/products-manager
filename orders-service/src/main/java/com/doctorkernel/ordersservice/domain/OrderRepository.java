package com.doctorkernel.ordersservice.domain;

import com.doctorkernel.ordersservice.domain.projection.OrderEntity;

public interface OrderRepository {
    OrderEntity save(OrderEntity orderEntity);
}
