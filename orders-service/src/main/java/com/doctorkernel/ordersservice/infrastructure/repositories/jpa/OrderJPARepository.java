package com.doctorkernel.ordersservice.infrastructure.repositories.jpa;

import com.doctorkernel.ordersservice.domain.projection.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderJPARepository extends JpaRepository<OrderEntity, String>{
    Optional<OrderEntity> findByOrderId(String orderId);
    void deleteByOrderId(String orderId);
}
