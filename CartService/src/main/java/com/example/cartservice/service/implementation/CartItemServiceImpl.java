package com.example.cartservice.service.implementation;

import com.example.cartservice.repository.CartItemRepository;
import com.example.cartservice.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private  final CartItemRepository cartItemRepository;
}
