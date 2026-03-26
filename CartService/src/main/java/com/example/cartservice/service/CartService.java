package com.example.cartservice.service;

import com.example.cartservice.dto.*;

public interface CartService {

    CartResponseDTO createCart(CartCreateRequest cartCreateRequest);

    CartResponseDTO getCart(Long userId);

    CartResponseDTO addItem(String userId, CartItemAddRequest cartItemAddRequest);

    CartResponseDTO clearCartByUserId(Long userId);
}
