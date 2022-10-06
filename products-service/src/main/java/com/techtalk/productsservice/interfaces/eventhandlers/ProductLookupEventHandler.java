package com.techtalk.productsservice.interfaces.eventhandlers;

import com.techtalk.productsservice.domain.ProductLookupRepository;
import com.techtalk.productsservice.domain.events.ProductCreatedEvent;
import com.techtalk.productsservice.domain.projection.ProductEntity;
import com.techtalk.productsservice.domain.projection.ProductLookupEntity;
import lombok.AllArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ResetHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@ProcessingGroup("product-group")
public class ProductLookupEventHandler {

    private final ProductLookupRepository productLookupRepository;

    @EventHandler
    public void on(ProductCreatedEvent productCreatedEvent) {
        ProductLookupEntity productLookupEntity= new ProductLookupEntity(productCreatedEvent.getProductId(), productCreatedEvent.getTitle());
        productLookupRepository.save(productLookupEntity);
    }

    @ResetHandler
    public void reset(){
        productLookupRepository.deleteAll();
    }
}
