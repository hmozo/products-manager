package com.techtalk.productsservice.infrastructure.repositories.jpa;

import com.techtalk.productsservice.domain.projection.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJPARepository extends JpaRepository<ProductEntity, String> {
}
