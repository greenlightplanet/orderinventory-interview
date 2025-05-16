package com.ecommerce.orderinventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ProductDetails {

    private Long productId;
    private String productName;
    private String productCategory;
    private int quantity;
    private BigDecimal unitPrice;

}
