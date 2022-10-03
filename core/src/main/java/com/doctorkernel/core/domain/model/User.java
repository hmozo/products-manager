package com.doctorkernel.core.domain.model;

import com.doctorkernel.core.domain.valueobjects.PaymentDetails;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private final String firstName;
    private final String lastName;
    private final String userId;
    private final PaymentDetails paymentDetails;
}
