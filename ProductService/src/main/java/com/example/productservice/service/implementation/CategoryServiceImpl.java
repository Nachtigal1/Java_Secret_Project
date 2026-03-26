package com.example.productservice.service.implementation;

import com.example.productservice.dto.*;
import com.example.productservice.exception.*;
import com.example.productservice.model.*;
import com.example.productservice.repository.CategoryRepository;
import com.example.productservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

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

    @Override
    public CategoryResponseDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new CategoryNotFoundException("Category with id " + id + " not found")
        );
        List<Product> products = category.getProducts();
        List<ProductResponseDTO> dtos = new ArrayList<>();
        if (!products.isEmpty()) {
            for (Product product : products) {
                ProductResponseDTO dto = new ProductResponseDTO();
                dto.setId(product.getId());
                dto.setName(product.getName());
                dto.setPrice(product.getPrice().toString());
                dtos.add(dto);
            }
        } else {
            dtos = new ArrayList<>();
        }

        return new CategoryResponseDTO(category.getId(), category.getName(), dtos);
    }
}
