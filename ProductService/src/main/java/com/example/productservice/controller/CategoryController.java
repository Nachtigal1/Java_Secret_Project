package com.example.productservice.controller;

import com.example.productservice.dto.*;
import com.example.productservice.service.implementation.CategoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryServiceImpl categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory (
            @RequestBody CategoryCreateRequest categoryCreateRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(categoryCreateRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById (
            @PathVariable Long id
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getCategoryById(id));
    }
}
