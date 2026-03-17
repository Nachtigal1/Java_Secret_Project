package com.example.orderservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderResponseDTO {

    private Long id;

    private Long userId;

    private String status;

    private String deliveryMethod;

    private List<OrderItemResponseDTO> orderItems;

    @Data
    public static class OrderItemResponseDTO {

        private Long id;

        private Long productId;

        private Integer quantity;

        private Double price;
    }

    private Double total;
}
