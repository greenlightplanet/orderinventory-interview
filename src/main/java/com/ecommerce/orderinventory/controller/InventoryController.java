package com.ecommerce.orderinventory.controller;

import com.ecommerce.orderinventory.dto.ProductDetails;
import com.ecommerce.orderinventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/all")
    public ResponseEntity<List<ProductDetails>> getAllProducts() {
        List<ProductDetails> products = inventoryService.getAllProductDetails();
        return ResponseEntity.ok(products);
    }
}
