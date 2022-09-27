package com.techtalk.productsservice.infrastructure.repositories.jpa;

import com.techtalk.productsservice.domain.ProductRepository;
import com.techtalk.productsservice.domain.projection.ProductEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJPARepository productJPARepository;

    @Override
    public ProductEntity save(ProductEntity productEntity) {
        return productJPARepository.save(productEntity);
    }

    @Override
    public List<ProductEntity> findAll() {
        return productJPARepository.findAll();
    }

    @Override
    public ProductEntity findByProductId(String productId) {
        return productJPARepository.findByProductId(productId);
    }


}
