package com.example.productservice.controller;

import com.example.productservice.dto.ProductResponseDTO;
import com.example.productservice.service.implementation.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/products")
@RequiredArgsConstructor
public class InternalProductController {

    private final ProductServiceImpl productService;

    @GetMapping("/exists/{id}")
    public Boolean existsById(@PathVariable Long id) {
        return productService.existsById(id);
    }

    @GetMapping("/quantity/{id}")
    public Integer getQuantityById(@PathVariable Long id) {
        return productService.getQuantityById(id);
    }

    @PutMapping("/quantity/{id}/{quantity}")
    public void updateQuantityById(@PathVariable String id, @PathVariable String quantity) {
        productService.updateProductQuantityById(
                Long.valueOf(id),
                Integer.valueOf(quantity)
        );
    }

    @GetMapping("/{id}")
    public ProductResponseDTO getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }
}
