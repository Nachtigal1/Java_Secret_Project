package com.example.orderservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartResponseDTO {

    private Long id;
    private Long userId;
    private List<CartItemResponseDTO> cartItems;

    @Data
    public static class CartItemResponseDTO {
        private Long productId;
        private Integer quantity;
        private Double price;
    }
}
