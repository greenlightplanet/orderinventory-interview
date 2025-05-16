package com.ecommerce.orderinventory.service;

import com.ecommerce.orderinventory.dto.ProductDetails;
import com.ecommerce.orderinventory.entity.Inventory;
import com.ecommerce.orderinventory.exception.InsufficientInventoryException;
import com.ecommerce.orderinventory.exception.ResourceNotFoundException;
import com.ecommerce.orderinventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public boolean restoreInventory(Long productId, int quantity) {
        Optional<Inventory> inventoryOpt = inventoryRepository.findByProductId(productId);
        if (inventoryOpt.isEmpty()) {
            log.error("Inventory not found for Product ID {}.", productId);
            return false;
        }
        Inventory inventory = inventoryOpt.get();
        inventory.setQuantity(inventory.getQuantity() + quantity);
        inventory.setUpdatedAt(LocalDateTime.now());
        inventoryRepository.save(inventory);
        log.info("Inventory restored for Product ID {}.", productId);
        return true;
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

    public void deductInventory(Long productId, int quantity) {
        Optional<Inventory> inventoryOpt = inventoryRepository.findByProductId(productId);
        Inventory inventory = inventoryOpt.orElseThrow(() ->
                new ResourceNotFoundException("Inventory not found for product ID: " + productId)
        );

        if (inventory.getQuantity() < quantity) {
            throw new InsufficientInventoryException("Insufficient inventory for product ID: " + productId);
        }
        inventory.setQuantity(inventory.getQuantity() - quantity);
        inventory.setUpdatedAt(LocalDateTime.now());
        inventoryRepository.save(inventory);
    }
}
