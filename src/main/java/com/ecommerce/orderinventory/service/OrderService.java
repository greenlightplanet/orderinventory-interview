package com.ecommerce.orderinventory.service;


import com.ecommerce.orderinventory.dto.CreateOrderRequest;
import com.ecommerce.orderinventory.dto.ProductDetails;
import com.ecommerce.orderinventory.entity.Orders;
import com.ecommerce.orderinventory.exception.InsufficientInventoryException;
import com.ecommerce.orderinventory.exception.InventoryServiceException;
import com.ecommerce.orderinventory.exception.OrderCancellationException;
import com.ecommerce.orderinventory.exception.ResourceNotFoundException;
import com.ecommerce.orderinventory.repository.OrdersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.json.JSONObject;
import org.json.JSONException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrdersRepository ordersRepository;
    private final InventoryService inventoryService;
    private final DeliveryService deliveryService;

    @Transactional
    public Orders createOrder( CreateOrderRequest request) throws Exception {
        try {
            List<ProductDetails> productDetailsList = inventoryService.getAllProductDetails();
            boolean productFound = false;

            for (ProductDetails product : productDetailsList) {
                if (product.getProductId().equals(request.getProductId())) {
                    productFound = true;
                    if (product.getQuantity() < request.getQuantity()) {
                        throw new InsufficientInventoryException("Insufficient inventory for product: " + request.getProductId());
                    }
                    break;
                }
            }

            if (!productFound) {
                throw new ResourceNotFoundException("Product not found: " + request.getProductId());
            }

            String estimatedDeliveryDateStr = deliveryService.getEstimatedDeliveryDate(request.getPinCode());
            LocalDate estimatedDeliveryDate = validateDeliveryResponse(estimatedDeliveryDateStr, request.getPinCode());

            Orders order = new Orders();
            order.setProductId(request.getProductId());
            order.setQuantity(request.getQuantity());
            order.setCustomerId(request.getCustomerId());
            order.setPaymentMethod(request.getPaymentMethod());
            order.setOrderDate(LocalDateTime.now());
            order.setStatus("CONFIRMED");
            order.setExpectedDelivery(estimatedDeliveryDate.atStartOfDay());

            inventoryService.deductInventory(request.getProductId(), request.getQuantity());

            return ordersRepository.save(order);
        } catch (InsufficientInventoryException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("Failed to create order: " + e.getMessage(), e);
        }
    }

    public LocalDate validateDeliveryResponse(String responseJsonStr, int pinCode) {
        if (responseJsonStr == null || responseJsonStr.trim().isEmpty()) {
            throw new ResourceNotFoundException("Could not ship to this location: " + pinCode);
        }

        try {
            JSONObject responseJson = new JSONObject(responseJsonStr);
            if (!responseJson.has("status") || !responseJson.getString("status").equalsIgnoreCase("success")) {
                throw new ResourceNotFoundException("Invalid response for delivery estimation.");
            }

            JSONObject deliveryInfo = responseJson.optJSONObject("deliveryInfo");
            if (deliveryInfo == null || !deliveryInfo.has("pincode") || !deliveryInfo.has("estimate")) {
                throw new ResourceNotFoundException("Missing delivery information for the location: " + pinCode);
            }

            JSONObject estimate = deliveryInfo.getJSONObject("estimate");
            if (!estimate.has("estimatedDeliveryDate")) {
                throw new ResourceNotFoundException("Estimated delivery date not found for the location: " + pinCode);
            }

            String estimatedDeliveryDate = estimate.getString("estimatedDeliveryDate");
            if (estimatedDeliveryDate.trim().isEmpty()) {
                throw new ResourceNotFoundException("Invalid estimated delivery date for the location: " + pinCode);
            }
            log.info("date of delivery {}", estimate.get("estimatedDeliveryDate"));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(estimatedDeliveryDate, formatter);
        } catch (JSONException e) {
            throw new ResourceNotFoundException("Invalid response format for delivery estimation.");
        }
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        if (orderId == null) {
            log.error("Attempted to cancel order with null ID");
            throw new IllegalArgumentException("Order ID cannot be null");
        }

        try {
            log.info("Attempting to cancel order with ID: {}", orderId);

            Orders order = ordersRepository.findById(orderId)
                    .orElseThrow(() -> {
                        log.warn("Order not found with ID: {}", orderId);
                        return new ResourceNotFoundException("Order not found with ID: " + orderId);
                    });
            validateOrderCancellation(order);
            processOrderCancellation(order);
            log.info("Successfully cancelled order with ID: {}", orderId);

        } catch (ResourceNotFoundException ex) {
            log.error("Order cancellation failed - {}", ex.getMessage());
            throw ex;
        } catch (InventoryServiceException ex) {
            log.error("Inventory restoration failed for order ID {}: {}", orderId, ex.getMessage());
            throw new OrderCancellationException("Failed to restore inventory during cancellation", ex);
        } catch (Exception ex) {
            log.error("Unexpected error cancelling order ID {}: {}", orderId, ex.getMessage(), ex);
            throw new OrderCancellationException("Unexpected error during order cancellation", ex);
        }
    }

    private void validateOrderCancellation(Orders order) {
        if ("SHIPPED".equalsIgnoreCase(order.getStatus())) {
            log.warn("Order with ID {} cannot be cancelled as it's already shipped", order.getId());
            throw new OrderCancellationException("Shipped orders cannot be cancelled");
        }

        if ("CANCELLED".equalsIgnoreCase(order.getStatus())) {
            log.warn("Order with ID {} is already cancelled", order.getId());
            throw new OrderCancellationException("Order is already cancelled");
        }
    }

    private void processOrderCancellation(Orders order) {
        order.setStatus("CANCELLED");
        ordersRepository.save(order);

        try {
            boolean inventoryRestored = inventoryService.restoreInventory(
                    order.getProductId(),
                    order.getQuantity()
            );

            if (!inventoryRestored) {
                throw new InventoryServiceException("Inventory restoration failed");
            }
        } catch (InventoryServiceException ex) {
            order.setStatus("PENDING");
            ordersRepository.save(order);
            throw ex;
        }
    }
}
