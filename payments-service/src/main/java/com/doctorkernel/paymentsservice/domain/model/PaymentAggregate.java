package com.doctorkernel.paymentsservice.domain.model;

import com.doctorkernel.core.domain.commands.ProcessPaymentCommand;
import com.doctorkernel.core.domain.events.PaymentProcessedEvent;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@NoArgsConstructor
public class PaymentAggregate {
    @AggregateIdentifier
    private String paymentId;
    private String orderId;

    @CommandHandler
    public PaymentAggregate(ProcessPaymentCommand processPaymentCommand){
        if (processPaymentCommand.getPaymentDetails()==null){
            throw new IllegalArgumentException("Missing payment details");
        }

        if (processPaymentCommand.getOrderId()==null){
            throw new IllegalArgumentException("Missing orderId");
        }

        if (processPaymentCommand.getPaymentId()==null){
            throw new IllegalArgumentException(("Missing paymentId"));
        }

        AggregateLifecycle.apply(PaymentProcessedEvent
                .builder().orderId(processPaymentCommand.getOrderId()).paymentId(processPaymentCommand.getPaymentId()).build());
    }

    @EventSourcingHandler
    protected void on(PaymentProcessedEvent paymentProcessedEvent){
        this.paymentId= paymentProcessedEvent.getPaymentId();
        this.orderId= paymentProcessedEvent.getOrderId();
    }
}
