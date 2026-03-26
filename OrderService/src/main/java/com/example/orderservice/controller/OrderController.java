package com.example.orderservice.controller;

import com.example.orderservice.dto.*;
import com.example.orderservice.service.implementation.OrderServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderServiceImpl orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder (
            @RequestHeader("X-User") String userId,
            @RequestBody OrderCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(Long.valueOf(userId), request));
    }
}
