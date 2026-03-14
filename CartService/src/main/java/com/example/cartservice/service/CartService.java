package com.example.cartservice.service;

import com.example.cartservice.dto.CartCreateRequest;
import com.example.cartservice.dto.CartItemAddRequest;
import com.example.cartservice.dto.CartResponseDTO;

public interface CartService {
    CartResponseDTO createCart(CartCreateRequest cartCreateRequest);
    CartResponseDTO getCart(Long userId);
    CartResponseDTO addItem(String userId, CartItemAddRequest cartItemAddRequest);
}
