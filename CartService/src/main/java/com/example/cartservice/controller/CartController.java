package com.example.cartservice.controller;

import com.example.cartservice.dto.CartCreateRequest;
import com.example.cartservice.dto.CartItemAddRequest;
import com.example.cartservice.dto.CartResponseDTO;
import com.example.cartservice.model.Cart;
import com.example.cartservice.service.implementation.CartServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartServiceImpl cartService;

    @PostMapping("/items")
    public ResponseEntity<CartResponseDTO> addItem (
            @RequestHeader("X-User") String userId,
            @RequestBody CartItemAddRequest cartItemAddRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addItem(userId, cartItemAddRequest));
    }
}
