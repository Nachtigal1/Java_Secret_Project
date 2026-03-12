package com.example.productservice.service.implementation;

import com.example.productservice.dto.CategoryCreateRequest;
import com.example.productservice.dto.CategoryResponseDTO;
import com.example.productservice.exception.CategoryAlreadyExistsException;
import com.example.productservice.model.Category;
import com.example.productservice.repository.CategoryRepository;
import com.example.productservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponseDTO createCategory(CategoryCreateRequest dto) {
        if (categoryRepository.existsByName(dto.getName())) {
            throw new CategoryAlreadyExistsException("Category with name " + dto.getName() + " already exists");
        }
        Category category = new Category();
        category.setName(dto.getName());

        categoryRepository.save(category);

        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();

        categoryResponseDTO.setId(category.getId());
        categoryResponseDTO.setName(category.getName());

        return categoryResponseDTO;
    }
}
