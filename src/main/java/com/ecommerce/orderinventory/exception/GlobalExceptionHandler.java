package com.ecommerce.orderinventory.exception;

import com.ecommerce.orderinventory.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleNotFound(ResourceNotFoundException ex) {
        return new ResponseEntity<>(new ApiResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InsufficientInventoryException.class)
    public ResponseEntity<ApiResponse> handleInventory(InsufficientInventoryException ex) {
        return new ResponseEntity<>(new ApiResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleAll(Exception ex) throws Exception {
        if(!ex.getClass().getName().equals("HttpRequestMethodNotSupportedException")){
            throw ex;
        }
        return new ResponseEntity<>(new ApiResponse("\"Something went wrong\""), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
