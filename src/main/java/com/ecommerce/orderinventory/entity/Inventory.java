package com.ecommerce.orderinventory.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory")
@Data
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;
    private String productName;
    private String productCategory;
    private int quantity;
    private BigDecimal unitPrice;
    private String supplier;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
