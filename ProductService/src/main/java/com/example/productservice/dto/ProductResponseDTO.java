package com.example.productservice.dto;

import lombok.Data;

@Data
public class ProductResponseDTO {

    private Long id;

    private String name;

    private String description;

    private String price;

    private String image;

    private String categoryName;
}
