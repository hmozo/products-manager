package com.doctorkernel.ordersservice.interfaces.rest;

import com.doctorkernel.ordersservice.domain.commands.CreateOrderCommand;
import com.doctorkernel.ordersservice.domain.util.GeneralConstants;
import com.doctorkernel.ordersservice.domain.util.OrderStatus;
import com.doctorkernel.ordersservice.domain.util.OrderSummary;
import com.doctorkernel.ordersservice.interfaces.eventHandlers.FindOrderQuery;
import com.doctorkernel.ordersservice.interfaces.rest.transform.dto.CreateOrderRequestDTO;
import lombok.AllArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.hibernate.id.UUIDGenerator;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderCommandController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    @PostMapping
    public OrderSummary createOrder(@Valid @RequestBody CreateOrderRequestDTO createOrderRequestDTO){

        String orderId= UUID.randomUUID().toString();

        CreateOrderCommand createOrderCommand= CreateOrderCommand.builder()
                .orderId(orderId)
                .userId(GeneralConstants.USER_ID)
                .productId(createOrderRequestDTO.getProductId())
                .quantity(createOrderRequestDTO.getQuantity())
                .addressId(createOrderRequestDTO.getAddressId())
                .orderStatus(OrderStatus.CREATED).build();

        var subscriptionQueryResult= queryGateway.subscriptionQuery(FindOrderQuery.builder().orderId(orderId).build(),
                ResponseTypes.instanceOf(OrderSummary.class),
                ResponseTypes.instanceOf(OrderSummary.class));

        commandGateway.sendAndWait(createOrderCommand);

        try {
            return subscriptionQueryResult.updates().blockFirst();
        }finally{
           subscriptionQueryResult.close();
        }
    }
}
