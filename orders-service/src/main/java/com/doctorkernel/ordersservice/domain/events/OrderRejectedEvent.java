package com.doctorkernel.ordersservice.domain.events;

import com.doctorkernel.ordersservice.domain.util.OrderStatus;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class OrderRejectedEvent {
    private final String orderId;
    private final String reason;
    private final OrderStatus orderStatus= OrderStatus.REJECTED;
}
