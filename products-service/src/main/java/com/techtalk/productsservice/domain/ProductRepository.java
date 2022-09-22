package com.techtalk.productsservice.domain;

import com.techtalk.productsservice.domain.projection.ProductEntity;

import java.util.List;

public interface ProductRepository {
    ProductEntity saveProduct(ProductEntity productEntity);
    List<ProductEntity> findAll();
}
