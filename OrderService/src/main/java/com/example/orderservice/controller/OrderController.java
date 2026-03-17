package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderCreateRequest;
import com.example.orderservice.dto.OrderResponseDTO;
import com.example.orderservice.service.implementation.OrderServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
