package com.example.cartservice.service.implementation;

import com.example.cartservice.dto.*;
import com.example.cartservice.model.*;
import com.example.cartservice.repository.*;
import com.example.cartservice.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public CartResponseDTO createCart(CartCreateRequest cartCreateRequest) {
        Cart cart = new Cart();
        List<CartItem> cartItems = new ArrayList<>();

        if (!cartCreateRequest.getCartItems().isEmpty()) {
            for (CartCreateRequest.CartItemCreateRequest item : cartCreateRequest.getCartItems()) {
                CartItem cartItem = new CartItem();
                cartItem.setProductId(item.getProductId());
                cartItem.setQuantity(item.getQuantity());
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
        return null;
    }

    @Override
    public CartResponseDTO addItem(String userId, CartItemAddRequest cartItemAddRequest) {
        Cart cart = cartRepository.findByUserId(Long.valueOf(userId)).orElseThrow(() -> new RuntimeException("Cart not found"));
        List<CartItem> cartItems = cart.getCartItems();

        CartItem cartItem = new CartItem();
        cartItem.setProductId(cartItemAddRequest.getProductId());
        cartItem.setQuantity(cartItemAddRequest.getQuantity());
        cartItem.setCart(cart);
        cartItems.add(cartItem);

        cart.setCartItems(cartItems);
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
                cartItemResponseDTOList.add(cartItemResponseDTO);
            }
        }

        cartResponseDTO.setCartItems(cartItemResponseDTOList);
        return cartResponseDTO;
    }
}
