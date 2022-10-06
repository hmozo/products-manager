package com.doctorkernel.ordersservice.domain.commands;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class ApproveOrderCommand {
    @TargetAggregateIdentifier
    private final String orderId;

}
