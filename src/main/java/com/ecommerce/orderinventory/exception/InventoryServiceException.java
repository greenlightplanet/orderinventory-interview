package com.ecommerce.orderinventory.exception;

public class InventoryServiceException extends RuntimeException {
    public InventoryServiceException(String message) {
        super(message);
    }
}