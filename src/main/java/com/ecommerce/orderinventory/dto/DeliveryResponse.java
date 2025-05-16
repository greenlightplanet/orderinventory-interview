package com.ecommerce.orderinventory.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DeliveryResponse {
    @JsonProperty("estimatedDeliveryDate")
    private LocalDate estimatedDeliveryDate;
}