package com.doctorkernel.ordersservice.interfaces.eventHandlers;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class FindOrderQuery {
    private final String orderId;
}
