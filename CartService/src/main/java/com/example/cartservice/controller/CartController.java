package com.example.cartservice.controller;

import com.example.cartservice.dto.*;
import com.example.cartservice.service.implementation.CartServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartServiceImpl cartService;

    @PostMapping
    public ResponseEntity<CartResponseDTO> createCart(
            @RequestBody CartCreateRequest cartCreateRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cartService.createCart(cartCreateRequest));
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponseDTO> addItem (
            @RequestHeader("X-User") String userId,
            @RequestBody CartItemAddRequest cartItemAddRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addItem(userId, cartItemAddRequest));
    }

    @GetMapping
    public ResponseEntity<CartResponseDTO> getCartByUserId (
            @RequestHeader("X-User") String userId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(cartService.getCart(Long.parseLong(userId)));
    }
    @DeleteMapping("/clear")
    public ResponseEntity<CartResponseDTO> clearCart(
            @RequestHeader("X-User") String userId
    ) {
        return ResponseEntity.ok(cartService.clearCartByUserId(Long.parseLong(userId)));
    }
}
