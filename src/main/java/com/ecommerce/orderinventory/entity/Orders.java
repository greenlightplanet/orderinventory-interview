package com.ecommerce.orderinventory.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;
    private Long productId;
    private int quantity;
    private LocalDateTime orderDate;
    private String status;
    private String paymentMethod;
    private LocalDateTime expectedDelivery;

}
