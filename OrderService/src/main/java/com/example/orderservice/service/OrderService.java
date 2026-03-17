package com.example.orderservice.service;

import com.example.orderservice.dto.*;

public interface OrderService {

    OrderResponseDTO createOrder(Long userId, OrderCreateRequest request);
}
