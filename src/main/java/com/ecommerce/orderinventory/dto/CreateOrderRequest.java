package com.ecommerce.orderinventory.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {
    @NotNull(message = "Product ID cannot be null")
    private Long productId;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Customer ID cannot be null")
    private Long customerId;

    @NotNull(message = "Pin code cannot be null")
    @Digits(integer = 6, fraction = 0, message = "Pin code must be a 6-digit number")
    private Integer pinCode;

    @NotBlank(message = "Payment method cannot be blank")
    private String paymentMethod;
}