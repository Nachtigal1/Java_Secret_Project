package com.example.cartservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartCreateRequest {

    private Long userId;
    private List<CartItemCreateRequest> cartItems;

    @Data
    public static class CartItemCreateRequest {
        private Long productId;
        private Integer quantity;
        private Double price;
    }
}
