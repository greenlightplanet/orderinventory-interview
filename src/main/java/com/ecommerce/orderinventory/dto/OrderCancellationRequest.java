package com.ecommerce.orderinventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderCancellationRequest {

    private Long orderId;

}
