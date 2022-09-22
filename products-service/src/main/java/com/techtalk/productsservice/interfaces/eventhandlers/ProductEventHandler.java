package com.techtalk.productsservice.interfaces.eventhandlers;

import com.techtalk.productsservice.application.querygateway.ProductsEventService;
import com.techtalk.productsservice.domain.events.ProductCreatedEvent;
import com.techtalk.productsservice.domain.projection.ProductEntity;
import lombok.AllArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@ProcessingGroup("product-group")
public class ProductEventHandler {

    private final ProductsEventService productsEventService;

    @ExceptionHandler(resultType = IllegalArgumentException.class)
    public void handle (IllegalArgumentException ex){
        System.out.println("error in eventHandler");
    }

    @ExceptionHandler(resultType = Exception.class)
    public void handle (Exception ex) throws Exception {
        throw ex;
    }

    @EventHandler
    public void on(ProductCreatedEvent productCreatedEvent) throws Exception {
        ProductEntity productEntity= new ProductEntity();
        BeanUtils.copyProperties(productCreatedEvent, productEntity);

        try {
            productsEventService.saveProduct(productEntity);
        }catch (IllegalArgumentException ex){
            ex.printStackTrace();
        }

        //if (true) throw new Exception("Error in ProductCreatedEvent event-handler");
    }
}
