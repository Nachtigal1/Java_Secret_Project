package com.example.productservice.service;

import com.example.productservice.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    ProductResponseDTO createProduct (String dto, MultipartFile image);

    ProductResponseDTO getProductById (Long id);

    List<ProductResponseDTO> getAllProducts ();

    Boolean existsById(Long id);

    Integer getQuantityById(Long id);

    void updateProductQuantityById(Long id, Integer quantity);

    void deleteProductById (Long id);
}
