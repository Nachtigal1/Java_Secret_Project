package com.example.cartservice.service.implementation;

import com.example.cartservice.dto.*;
import com.example.cartservice.model.*;
import com.example.cartservice.repository.*;
import com.example.cartservice.service.CartService;
import com.example.cartservice.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final RestTemplate restTemplate;

    @Value("${product-service.url}")
    private String productServiceUrl;

    @Override
    public CartResponseDTO createCart(CartCreateRequest cartCreateRequest) {
        Cart cart = new Cart();
        List<CartItem> cartItems = new ArrayList<>();

        if (!cartCreateRequest.getCartItems().isEmpty()) {
            for (CartCreateRequest.CartItemCreateRequest item : cartCreateRequest.getCartItems()) {
                CartItem cartItem = new CartItem();
                cartItem.setProductId(item.getProductId());
                cartItem.setQuantity(item.getQuantity());
                cartItem.setPrice(item.getPrice());
                cartItems.add(cartItem);
            }
        }

        cart.setUserId(cartCreateRequest.getUserId());
        cart.setCartItems(cartItems);

        cartRepository.save(cart);

        return getCartResponseDTO(cart);
    }

    @Override
    public CartResponseDTO getCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));
        return getCartResponseDTO(cart);
    }

    @Override
    @Transactional
    public CartResponseDTO addItem(String userId, CartItemAddRequest request) {

        ProductResponseDTO product;

        try {
            product = restTemplate.getForObject(
                    productServiceUrl + "/internal/products/" + request.getProductId(),
                    ProductResponseDTO.class
            );
        } catch (HttpClientErrorException.NotFound ex) {
            throw new ProductNotFoundException("Product not found");
        }

        Cart cart = cartRepository.findByUserId(Long.valueOf(userId))
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));

        List<CartItem> cartItems = cart.getCartItems();

        Optional<CartItem> existingItem = cartItems.stream()
                .filter(item -> item.getProductId().equals(request.getProductId()))
                .findFirst();

        if (existingItem.isPresent()) {

            CartItem item = existingItem.get();
            int newQuantity = item.getQuantity() + request.getQuantity();

            if (newQuantity > product.getQuantity()) {
                throw new NotEnoughStockException("Not enough quantity in stock");
            }

            item.setQuantity(newQuantity);

        } else {

            if (request.getQuantity() > product.getQuantity()) {
                throw new NotEnoughStockException("Not enough quantity in stock");
            }

            CartItem cartItem = new CartItem();
            cartItem.setProductId(request.getProductId());
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(Double.valueOf(product.getPrice()));
            cartItem.setCart(cart);

            cartItems.add(cartItem);
        }

        cartRepository.save(cart);

        return getCartResponseDTO(cart);
    }

    @Override
    public CartResponseDTO clearCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));
        cart.getCartItems().clear();
        cartRepository.save(cart);
        return getCartResponseDTO(cart);
    }

    private static CartResponseDTO getCartResponseDTO(Cart cart) {
        CartResponseDTO cartResponseDTO = new CartResponseDTO();
        cartResponseDTO.setId(cart.getId());
        cartResponseDTO.setUserId(cart.getUserId());
        List<CartResponseDTO.CartItemResponseDTO> cartItemResponseDTOList = new ArrayList<>();

        if (!cart.getCartItems().isEmpty()) {
            for (CartItem item : cart.getCartItems()) {
                CartResponseDTO.CartItemResponseDTO cartItemResponseDTO = new CartResponseDTO.CartItemResponseDTO();
                cartItemResponseDTO.setProductId(item.getProductId());
                cartItemResponseDTO.setQuantity(item.getQuantity());
                cartItemResponseDTO.setPrice(item.getPrice());
                cartItemResponseDTOList.add(cartItemResponseDTO);
            }
        }

        cartResponseDTO.setCartItems(cartItemResponseDTOList);
        return cartResponseDTO;
    }
}
