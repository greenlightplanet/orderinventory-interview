package com.ecommerce.orderinventory.exception;

public class OrderCancellationException extends RuntimeException {
    public OrderCancellationException(String message) {
        super(message);
    }
    public OrderCancellationException(String message, Throwable cause) {
        super(message, cause);
    }
}