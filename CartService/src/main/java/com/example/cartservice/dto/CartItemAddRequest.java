package com.example.cartservice.dto;

import lombok.Data;

@Data
public class CartItemAddRequest {

    private Long productId;

    private Integer quantity;
}
