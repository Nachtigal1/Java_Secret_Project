package com.example.productservice.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponseDTO {

    private Long id;

    private String name;

    private List<ProductResponseDTO> products = new ArrayList<>();
}
