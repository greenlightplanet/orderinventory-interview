package com.ecommerce.orderinventory.service;

import com.ecommerce.orderinventory.dto.ProductDetails;
import com.ecommerce.orderinventory.entity.Inventory;
import com.ecommerce.orderinventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public void restoreInventory(Long productId, int quantity) {
        Optional<Inventory> inventory = inventoryRepository.findByProductId(productId);
        inventory.equals(inventory.get().getQuantity() - quantity);
        inventory.get().setUpdatedAt(LocalDateTime.now());
        inventoryRepository.save(inventory.get());
    }

    public List<ProductDetails> getAllProductDetails() {
        return inventoryRepository.findAll().stream()
                .map(inv -> new ProductDetails(
                        inv.getProductId(),
                        inv.getProductName(),
                        inv.getProductCategory(),
                        inv.getQuantity(),
                        inv.getUnitPrice()
                ))
                .collect(Collectors.toList());
    }
}
