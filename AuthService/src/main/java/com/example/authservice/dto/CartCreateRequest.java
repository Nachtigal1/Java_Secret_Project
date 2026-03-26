package com.example.authservice.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
public class CartCreateRequest {

    private Long userId;
    private List<CartItemCreateRequest> cartItems;

    @Data
    @AllArgsConstructor
    public static class CartItemCreateRequest {
        private Long productId;
        private Integer quantity;
    }
}
