package com.doctorkernel.ordersservice.domain.events;


import com.doctorkernel.ordersservice.domain.util.OrderStatus;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class OrderApprovedEvent {
    private final String orderId;
    private final OrderStatus orderStatus= OrderStatus.APPROVED;
}
