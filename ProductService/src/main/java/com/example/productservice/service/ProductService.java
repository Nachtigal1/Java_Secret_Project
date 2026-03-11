package com.example.productservice.service;

import com.example.productservice.dto.*;

import java.util.List;

public interface ProductService {

    ProductResponseDTO createProduct (ProductCreateDTO dto);

    ProductResponseDTO getProductById (Long id);

    List<ProductResponseDTO> getAllProducts ();

    void deleteProductById (Long id);
}
