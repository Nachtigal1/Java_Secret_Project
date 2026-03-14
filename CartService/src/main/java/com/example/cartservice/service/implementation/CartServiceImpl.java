package com.example.cartservice.service.implementation;

import com.example.cartservice.dto.CartCreateRequest;
import com.example.cartservice.dto.CartResponseDTO;
import com.example.cartservice.model.Cart;
import com.example.cartservice.model.CartItem;
import com.example.cartservice.repository.CartRepository;
import com.example.cartservice.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

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

    @Override
    public CartResponseDTO getCart(Long userId) {
        return null;
    }
}
