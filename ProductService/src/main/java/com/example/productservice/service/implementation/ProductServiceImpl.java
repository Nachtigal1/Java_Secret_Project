package com.example.productservice.service.implementation;

import com.example.productservice.dto.*;
import com.example.productservice.exception.*;
import com.example.productservice.model.Product;
import com.example.productservice.repository.CategoryRepository;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.service.ProductService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public ProductResponseDTO createProduct(ProductCreateDTO dto) {
        if (productRepository.existsByName(dto.getName())) {
            throw new ProductAlreadyExistsException("Product with name " + dto.getName() + " already exists");
        }

        Product product = new Product();

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(Double.parseDouble(dto.getPrice()));
        product.setImage(dto.getImage());
        product.setQuantity(100);
        product.setPrice(Double.parseDouble(dto.getPrice()));
        product.setCategory(categoryRepository.findById(dto.getCategoryId()).orElseThrow(
                () -> new CategoryNotFoundException("Category with id " + dto.getCategoryId() + " not found")
        ));

        productRepository.save(product);

        return getProductResponseDTO(product);
    }

    @Override
    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ProductNotFoundException("Product with id " + id + " not found")
        );

        return getProductResponseDTO(product);
    }

    @NonNull
    private ProductResponseDTO getProductResponseDTO(Product product) {
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();

        productResponseDTO.setId(product.getId());
        productResponseDTO.setName(product.getName());
        productResponseDTO.setDescription(product.getDescription());
        productResponseDTO.setPrice(product.getPrice().toString());
        productResponseDTO.setImage(product.getImage());
        productResponseDTO.setQuantity(product.getQuantity());
        productResponseDTO.setPrice(product.getPrice().toString());
        productResponseDTO.setCategoryName(product.getCategory().getName());

        return  productResponseDTO;
    }

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        if (productRepository.findAll().isEmpty()) {
            throw new ProductNotFoundException("Products not found");
        }

        List<Product> products = productRepository.findAll();
        List<ProductResponseDTO> productResponseDTOS = new ArrayList<>();
        for (Product product : products) {
            productResponseDTOS.add(getProductResponseDTO(product));
        }

        return productResponseDTOS;
    }

    @Override
    public Boolean existsById(Long id) {
        return productRepository.existsById(id);
    }

    @Override
    public Integer getQuantityById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ProductNotFoundException("Product with id " + id + " not found")
        );
        return product.getQuantity();
    }

    @Override
    public void updateProductQuantityById(Long id, Integer quantity) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ProductNotFoundException("Product with id " + id + " not found")
        );

        Integer currentQuantity = product.getQuantity();
        product.setQuantity(currentQuantity - quantity);
        productRepository.save(product);
    }

    @Override
    public void deleteProductById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product with id " + id + " not found");
        }

        productRepository.deleteById(id);
    }
}
