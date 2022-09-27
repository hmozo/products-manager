package com.doctorkernel.ordersservice.domain.commands;

import com.doctorkernel.ordersservice.domain.util.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import org.axonframework.modelling.command.TargetAggregateIdentifier;


@Data
@Builder
public class CreateOrderCommand {
    @TargetAggregateIdentifier
    public final String orderId;

    private final String userId;
    private final String productId;
    private final int quantity;
    private final String addressId;
    private final OrderStatus orderStatus;


}
