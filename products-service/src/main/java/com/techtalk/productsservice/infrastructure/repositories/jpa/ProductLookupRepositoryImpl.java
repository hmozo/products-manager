package com.techtalk.productsservice.infrastructure.repositories.jpa;

import com.techtalk.productsservice.domain.ProductLookupRepository;
import com.techtalk.productsservice.domain.projection.ProductLookupEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class ProductLookupRepositoryImpl implements ProductLookupRepository {

    ProductLookupJPARepository productLookupJPARepository;

    @Override
    public ProductLookupEntity findByTitle(String title) {
        return productLookupJPARepository.findByTitle(title);
    }

    @Override
    public void save(ProductLookupEntity productLookupEntity) {
        productLookupJPARepository.save(productLookupEntity);
    }
}
