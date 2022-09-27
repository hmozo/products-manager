package com.doctorkernel.ordersservice.infrastructure.repositories.jpa;

import com.doctorkernel.ordersservice.domain.projection.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJPARepository extends JpaRepository<OrderEntity, String>{
}
