package com.techtalk.productsservice.interfaces.rest;

import com.techtalk.productsservice.domain.commands.CreateProductCommand;
import com.techtalk.productsservice.interfaces.rest.transform.dto.CreateProductRestModel;
import lombok.AllArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/products") // http://localhost:8080/products
@AllArgsConstructor
public class ProductCommandController {

    private final Environment env;
    private final CommandGateway commandGateway;

    @PostMapping
    public String createProduct(@Valid @RequestBody CreateProductRestModel createProductRestModel) {

        CreateProductCommand createProductCommand = CreateProductCommand.builder()
                .price(createProductRestModel.getPrice())
                .quantity(createProductRestModel.getQuantity())
                .title(createProductRestModel.getTitle())
                .productId(UUID.randomUUID().toString()).build();

        return commandGateway.sendAndWait(createProductCommand);
    }

//	@GetMapping
//	public String getProduct() {
//		return "HTTP GET Handled " + env.getProperty("local.server.port");
//	}
//
//	@PutMapping
//	public String updateProduct() {
//		return "HTTP PUT Handled";
//	}
//
//	@DeleteMapping
//	public String deleteProduct() {
//		return "HTTP DELETE handled";
//	}

}
