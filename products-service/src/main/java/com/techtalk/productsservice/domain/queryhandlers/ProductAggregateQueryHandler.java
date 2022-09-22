package com.techtalk.productsservice.domain.queryhandlers;

import com.techtalk.productsservice.domain.ProductRepository;
import com.techtalk.productsservice.domain.projection.ProductEntity;
import com.techtalk.productsservice.domain.queries.FindProductsQuery;
import com.techtalk.productsservice.interfaces.rest.transform.dto.ProductRestModel;
import lombok.AllArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ProductAggregateQueryHandler {
    public final ProductRepository productRepository;

    @QueryHandler
    List<ProductRestModel> findProducts(FindProductsQuery query){

        List<ProductRestModel> products= new ArrayList<>();
        List<ProductEntity> storedProducts= productRepository.findAll();

        products= storedProducts.stream()
                .map(p->{
                    ProductRestModel prm= new ProductRestModel();
                    BeanUtils.copyProperties(p, prm);
                    return prm;
                }).collect(Collectors.toList());

        return products;
    }
}
