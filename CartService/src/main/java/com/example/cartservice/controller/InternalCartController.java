package com.example.cartservice.controller;

import com.example.cartservice.dto.*;
import com.example.cartservice.service.implementation.CartServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

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
