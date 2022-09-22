package com.techtalk.productsservice.infrastructure.repositories.jpa;

import com.techtalk.productsservice.domain.projection.ProductLookupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductLookupJPARepository extends JpaRepository<ProductLookupEntity, String> {
    ProductLookupEntity findByTitle(String title);
}
