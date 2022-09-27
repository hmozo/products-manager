package com.doctorkernel.ordersservice.infrastructure.repositories.jpa;

import com.doctorkernel.ordersservice.domain.OrderRepository;
import com.doctorkernel.ordersservice.domain.projection.OrderEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJPARepository orderJPARepository;

    @Override
    public OrderEntity save(OrderEntity orderEntity) {
        return orderJPARepository.save(orderEntity);
    }
}
