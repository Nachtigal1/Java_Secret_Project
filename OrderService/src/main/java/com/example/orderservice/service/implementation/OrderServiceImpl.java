package com.example.orderservice.service.implementation;

import com.example.orderservice.dto.*;
import com.example.orderservice.model.*;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    @Value("${cart-service.url}")
    private String cartServiceUrl;
    @Value("${product-service.url}")
    private String productServiceUrl;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderResponseDTO createOrder(Long userId, OrderCreateRequest request) {
        CartResponseDTO cartResponseDTO;

        try {
            cartResponseDTO = restTemplate.getForObject(
                    cartServiceUrl + "/internal/carts/users/" + userId,
                    CartResponseDTO.class
            );
            log.info("Cart service response: {}", cartResponseDTO);
        } catch (Exception ex) {
            throw new RuntimeException("Cart service unavailable");
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setStatus(Order.Status.PENDING);
        order.setDeliveryMethod(Order.DeliveryMethod.DELIVERY);
        order.setTotal(0.0);
        order.setOrderItems(new ArrayList<>());

        Order savedOrder = orderRepository.save(order);
        log.info("Order saved with id: {}", savedOrder.getId());

        List<OrderItem> orderItems = new ArrayList<>();

        if (cartResponseDTO != null && cartResponseDTO.getCartItems() != null) {
            for (CartResponseDTO.CartItemResponseDTO cartItem : cartResponseDTO.getCartItems()) {
                OrderItem orderItem = new OrderItem();
                orderItem.setProductId(cartItem.getProductId());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setPrice(cartItem.getPrice());
                orderItem.setOrder(savedOrder);

                try {
                    restTemplate.put(
                            productServiceUrl + "/internal/products/quantity/" + orderItem.getProductId() + "/" + orderItem.getQuantity(),
                            null,
                            Void.class
                    );
                    log.info("Product quantity updated for productId: {}", orderItem.getProductId());
                } catch (Exception ex) {
                    log.error("Failed to update product quantity for productId: {}", orderItem.getProductId(), ex);
                }

                orderItems.add(orderItem);
            }
        }

        double total = orderItems.stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();

        savedOrder.setOrderItems(orderItems);
        savedOrder.setTotal(total);
        orderRepository.save(savedOrder);
        log.info("Order updated with items, total: {}", total);

        try {
            restTemplate.delete(cartServiceUrl + "/internal/carts/clear/" + userId);
            log.info("Cart cleared for user: {}", userId);
        } catch (Exception ex) {
            log.error("Failed to clear cart for user: {}", userId, ex);
        }

        return getOrderResponseDTO(userId, savedOrder);
    }

    private static OrderResponseDTO getOrderResponseDTO(Long userId, Order order) {
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setId(order.getId());
        orderResponseDTO.setOrderItems(getOrderItemResponseDTOS(order));
        orderResponseDTO.setUserId(userId);
        orderResponseDTO.setStatus(order.getStatus().name());
        orderResponseDTO.setDeliveryMethod(order.getDeliveryMethod().name());
        orderResponseDTO.setTotal(order.getTotal());
        return orderResponseDTO;
    }

    private static List<OrderResponseDTO.OrderItemResponseDTO> getOrderItemResponseDTOS(Order order) {
        List<OrderResponseDTO.OrderItemResponseDTO> list = new ArrayList<>();
        if (order.getOrderItems() != null) {
            for (OrderItem item : order.getOrderItems()) {
                OrderResponseDTO.OrderItemResponseDTO dto = new OrderResponseDTO.OrderItemResponseDTO();
                dto.setProductId(item.getProductId());
                dto.setQuantity(item.getQuantity());
                dto.setPrice(item.getPrice());
                list.add(dto);
            }
        }
        return list;
    }
}