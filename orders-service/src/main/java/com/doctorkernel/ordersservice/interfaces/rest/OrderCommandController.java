package com.doctorkernel.ordersservice.interfaces.rest;

import com.doctorkernel.ordersservice.domain.commands.CreateOrderCommand;
import com.doctorkernel.ordersservice.domain.util.GeneralConstants;
import com.doctorkernel.ordersservice.domain.util.OrderStatus;
import com.doctorkernel.ordersservice.interfaces.rest.transform.dto.CreateOrderRequestDTO;
import lombok.AllArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.hibernate.id.UUIDGenerator;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderCommandController {

    private final CommandGateway commandGateway;

    @PostMapping
    public String createOrder(@Valid @RequestBody CreateOrderRequestDTO createOrderRequestDTO){
        CreateOrderCommand createOrderCommand= CreateOrderCommand.builder()
                .orderId(UUID.randomUUID().toString())
                .userId(GeneralConstants.USER_ID)
                .productId(createOrderRequestDTO.getProductId())
                .quantity(createOrderRequestDTO.getQuantity())
                .addressId(createOrderRequestDTO.getAddressId())
                .orderStatus(OrderStatus.CREATED).build();

        return commandGateway.sendAndWait(createOrderCommand);
    }
}
