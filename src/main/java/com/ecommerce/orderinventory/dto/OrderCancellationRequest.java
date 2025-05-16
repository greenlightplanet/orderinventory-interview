package com.ecommerce.orderinventory.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderCancellationRequest {

    @NotNull(message = "orderId could not be null")
    private Long orderId;


}
