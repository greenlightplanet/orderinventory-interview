package com.ecommerce.orderinventory.dto;

import com.ecommerce.orderinventory.entity.Orders;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderResponse {
    private Long orderId;
    private Long customerId;
    private Long productId;
    private Integer quantity;
    private LocalDateTime orderDate;
    private String status;
    private String paymentMethod;
    private LocalDateTime expectedDelivery;

    public CreateOrderResponse(Orders order) {
        this.orderId=order.getId();
        this.customerId = order.getCustomerId();
        this.productId = order.getProductId();
        this.quantity = order.getQuantity();
        this.orderDate = order.getOrderDate();
        this.status = order.getStatus();
        this.paymentMethod = order.getPaymentMethod();
        this.expectedDelivery = order.getExpectedDelivery();
    }
}
