package com.techtalk.productsservice.application.querygateway;

import com.techtalk.productsservice.domain.ProductRepository;
import com.techtalk.productsservice.domain.projection.ProductEntity;
import org.springframework.stereotype.Service;

@Service
public class ProductsEventService {

    private final ProductRepository productRepository;

    public ProductsEventService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void saveProduct(ProductEntity productEntity){
        productRepository.saveProduct(productEntity);
    }
}
