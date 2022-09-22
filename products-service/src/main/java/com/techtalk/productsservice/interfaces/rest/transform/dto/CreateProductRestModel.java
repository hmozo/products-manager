package com.techtalk.productsservice.interfaces.rest.transform.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
public class CreateProductRestModel {
    @NotBlank(message = "Product title is required")
    private String title;

    @Min(value = 1, message = "Price cannot be lower than 1")
    private BigDecimal price;
    @Min(value = 1, message = "quantity cannot be lower than 1")
    @Max(value = 5, message = "quantity cannot be greater than 5")
    private Integer quantity;
}
