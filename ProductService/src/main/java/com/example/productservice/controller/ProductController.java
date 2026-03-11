package com.example.productservice.controller;

import com.example.productservice.dto.*;
import com.example.productservice.service.implementation.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductServiceImpl productService;

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct (
            @RequestBody ProductCreateDTO productCreateDTO
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(productCreateDTO));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts () {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById (
            @PathVariable Long id
    ) {
        return  ResponseEntity.status(HttpStatus.OK).body(productService.getProductById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProductById (
            @PathVariable Long id
    ) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
