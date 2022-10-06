package com.techtalk.productsservice.domain;

import com.techtalk.productsservice.domain.projection.ProductLookupEntity;

public interface ProductLookupRepository {
    ProductLookupEntity findByTitle(String title);
    void save(ProductLookupEntity productLookupEntity);
    void deleteAll();
}
