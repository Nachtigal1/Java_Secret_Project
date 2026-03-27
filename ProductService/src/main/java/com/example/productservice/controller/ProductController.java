package com.example.productservice.controller;

import com.example.productservice.dto.*;
import com.example.productservice.service.implementation.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")


public class ProductController {

    private final ProductServiceImpl productService;

    @PostMapping
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDTO> createProduct (
            @RequestPart("data") String productCreateDTO,
            @RequestPart("file") MultipartFile image
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(productCreateDTO, image));
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
        productService.deleteProductById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
