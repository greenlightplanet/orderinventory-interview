package com.ecommerce.orderinventory.service;

import com.ecommerce.orderinventory.dto.DeliveryResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class DeliveryService {

    private static final String DELIVERY_API_URL = "https://delivery-mock-api.vercel.app/api/delivery?pincode=";
    private final RestTemplate restTemplate;

    public DeliveryService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getEstimatedDeliveryDate(int pinCode) {
        String url = DELIVERY_API_URL + pinCode;
        String response = restTemplate.getForObject(url, String.class);
        log.info("response for getEstimatedDeliveryDate:{}", response );
        return response ;
    }
}