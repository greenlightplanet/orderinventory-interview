package com.ecommerce.orderinventory.controller;

import com.ecommerce.orderinventory.dto.ApiResponse;
import com.ecommerce.orderinventory.dto.OrderCancellationRequest;
import com.ecommerce.orderinventory.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/cancel")
    public ResponseEntity<ApiResponse> cancelOrder(@RequestBody OrderCancellationRequest orderCancellationRequest) {
        orderService.cancelOrder(orderCancellationRequest.getOrderId());
        return ResponseEntity.ok(new ApiResponse("Order cancelled successfully"));
    }
}

