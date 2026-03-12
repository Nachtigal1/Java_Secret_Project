package com.example.productservice.dto;

import lombok.Data;

@Data
public class ProductCreateDTO {

    private String name;

    private String description;

    private String price;
}
