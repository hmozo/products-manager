package com.techtalk.productsservice.domain.errorhandlers;

import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventhandling.EventMessageHandler;
import org.axonframework.eventhandling.ListenerInvocationErrorHandler;

public class ProductServiceEventErrorHandler implements ListenerInvocationErrorHandler {
    @Override
    public void onError(Exception ex, EventMessage<?> eventMessage, EventMessageHandler eventMessageHandler) throws Exception {
        throw ex;
    }
}
