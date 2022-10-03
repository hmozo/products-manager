package com.doctorkernel.core.domain.events;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PaymentProcessedEvent {
    private final String orderId;
    private final String paymentId;
}
