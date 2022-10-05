package com.doctorkernel.ordersservice.domain.commands;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.AggregateIdentifier;

@Data
@Builder
public class ApproveOrderCommand {
    @AggregateIdentifier
    private final String orderId;

}
