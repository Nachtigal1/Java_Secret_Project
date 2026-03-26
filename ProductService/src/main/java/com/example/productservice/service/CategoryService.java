package com.example.productservice.service;

import com.example.productservice.dto.*;

public interface CategoryService {

    CategoryResponseDTO createCategory(CategoryCreateRequest dto);

    CategoryResponseDTO getCategoryById(Long id);
}
