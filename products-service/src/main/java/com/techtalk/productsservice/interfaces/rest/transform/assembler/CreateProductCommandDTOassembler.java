package com.techtalk.productsservice.interfaces.rest.transform.assembler;

import com.techtalk.productsservice.domain.commands.CreateProductCommand;
import com.techtalk.productsservice.interfaces.rest.transform.dto.CreateProductRestModel;
import java.util.UUID;

public class CreateProductCommandDTOassembler {

    public static CreateProductCommand toCommandFromDTO(CreateProductRestModel createProductRestModel){
        return CreateProductCommand.builder()
                .productId(UUID.randomUUID().toString())
                .title(createProductRestModel.getTitle())
                .price(createProductRestModel.getPrice())
                .quantity(createProductRestModel.getQuantity())
                .build();
    }
}
