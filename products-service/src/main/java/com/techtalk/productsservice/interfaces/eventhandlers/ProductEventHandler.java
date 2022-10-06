package com.techtalk.productsservice.interfaces.eventhandlers;

import com.doctorkernel.core.domain.events.ProductReservationCancelledEvent;
import com.doctorkernel.core.domain.events.ProductReservedEvent;
import com.techtalk.productsservice.application.querygateway.ProductsEventService;
import com.techtalk.productsservice.domain.ProductRepository;
import com.techtalk.productsservice.domain.events.ProductCreatedEvent;
import com.techtalk.productsservice.domain.projection.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ResetHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
@ProcessingGroup("product-group")
public class ProductEventHandler {

    private final ProductsEventService productsEventService;
    private final ProductRepository productRepository;

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
    }

    @EventHandler
    public void on(ProductReservedEvent productReservedEvent){
        ProductEntity productEntity= productsEventService.findProductById(productReservedEvent.getProductId());
        productEntity.setQuantity(productEntity.getQuantity()-productReservedEvent.getQuantity());
        productRepository.save(productEntity);

        log.info("ProductReservedEvent handled for orderId: " + productReservedEvent.getOrderId() + " and productId: " + productReservedEvent.getProductId());
    }

    @EventHandler
    public void on(ProductReservationCancelledEvent productReservationCancelledEvent){
        ProductEntity productEntity= productsEventService.findProductById(productReservationCancelledEvent.getProductId());

        int newQuantity= productEntity.getQuantity() + productReservationCancelledEvent.getQuantity();
        productEntity.setQuantity(newQuantity);
        productRepository.save(productEntity);
    }

    @ResetHandler
    public void reset(){
        productRepository.deleteAll();
    }
}
