package com.techtalk.productsservice.application.querygateway;

import com.techtalk.productsservice.domain.ProductRepository;
import com.techtalk.productsservice.domain.projection.ProductEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductsEventService {

    private final ProductRepository productRepository;

    public void saveProduct(ProductEntity productEntity){
        productRepository.save(productEntity);
    }

    public ProductEntity findProductById(String productId){
        return productRepository.findByProductId(productId);
    }
}
