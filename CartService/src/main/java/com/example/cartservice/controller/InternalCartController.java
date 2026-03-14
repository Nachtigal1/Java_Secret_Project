package com.example.cartservice.controller;

import com.example.cartservice.dto.CartCreateRequest;
import com.example.cartservice.dto.CartResponseDTO;
import com.example.cartservice.service.implementation.CartServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/carts")
@RequiredArgsConstructor
public class InternalCartController {

    private final CartServiceImpl cartService;

    @PostMapping
    public ResponseEntity<CartResponseDTO> createCart (
            @RequestBody CartCreateRequest cartCreateRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.createCart(cartCreateRequest));
    }
}
