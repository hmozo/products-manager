package com.techtalk.productsservice.domain.model;

import com.doctorkernel.core.domain.commands.ReserveProductCommand;
import com.doctorkernel.core.domain.events.ProductReservedEvent;
import com.techtalk.productsservice.domain.commands.CreateProductCommand;
import com.techtalk.productsservice.domain.events.ProductCreatedEvent;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Aggregate
@NoArgsConstructor
public class ProductAggregate {
    @AggregateIdentifier
    private String productId;
    private String title;
    private BigDecimal price;
    private Integer quantity;

    @CommandHandler
    public ProductAggregate(CreateProductCommand createProductCommand) throws Exception {

        if(createProductCommand.getPrice().compareTo(BigDecimal.ZERO)<=0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }

        if(createProductCommand.getTitle()==null || createProductCommand.getTitle().isBlank()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }

        ProductCreatedEvent productCreatedEvent= new ProductCreatedEvent();
        BeanUtils.copyProperties(createProductCommand, productCreatedEvent);

        AggregateLifecycle.apply(productCreatedEvent);
    }

    @CommandHandler
    public void handle(ReserveProductCommand reserveProductCommand){
        if(quantity<reserveProductCommand.getQuantity()){
            throw new IllegalArgumentException(("Insufficient number of items in stock"));
        }

        ProductReservedEvent productReservedEvent = ProductReservedEvent.builder()
                .orderId(reserveProductCommand.getOrderId())
                .productId(reserveProductCommand.getProductId())
                .quantity(reserveProductCommand.getQuantity())
                .userId(reserveProductCommand.getUserId())
                .build();

        AggregateLifecycle.apply(productReservedEvent);
    }

    @EventSourcingHandler
    public void on(ProductCreatedEvent productCreatedEvent) {
        this.productId= productCreatedEvent.getProductId();
        this.title= productCreatedEvent.getTitle();
        this.price= productCreatedEvent.getPrice();
        this.quantity= productCreatedEvent.getQuantity();
    }

    @EventSourcingHandler
    public void on(ProductReservedEvent productReservedEvent) {
        this.quantity -= productReservedEvent.getQuantity();
    }
}
