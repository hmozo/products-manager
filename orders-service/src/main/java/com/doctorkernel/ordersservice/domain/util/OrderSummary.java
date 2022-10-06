package com.doctorkernel.ordersservice.domain.util;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class OrderSummary {
    private final String orderId;
    private final OrderStatus orderStatus;
    private final String message;
}
