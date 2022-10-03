/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doctorkernel.UsersService.interfaces.eventHandlers;

import com.doctorkernel.core.domain.model.User;
import com.doctorkernel.core.domain.queries.FetchUserPaymentDetailsQuery;
import com.doctorkernel.core.domain.valueobjects.PaymentDetails;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;


@Component
public class UserEventsHandler {

    @QueryHandler
    public User findUserPaymentDetails(FetchUserPaymentDetailsQuery query) {
        
        PaymentDetails paymentDetails = PaymentDetails.builder()
                .cardNumber("123Card")
                .cvv("123")
                .name("SERGEY KARGOPOLOV")
                .validUntilMonth(12)
                .validUntilYear(2030)
                .build();

        User user = User.builder()
                .firstName("Sergey")
                .lastName("Kargopolov")
                .userId(query.getUserId())
                .paymentDetails(paymentDetails)
                .build();

        return user;
    }
    
    
}
