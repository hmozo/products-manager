package com.techtalk.productsservice.domain;

import com.techtalk.productsservice.domain.projection.ProductEntity;

import java.util.List;

public interface ProductRepository {
    ProductEntity save(ProductEntity productEntity);
    List<ProductEntity> findAll();
    ProductEntity findByProductId(String productId);
    void deleteAll();
}
