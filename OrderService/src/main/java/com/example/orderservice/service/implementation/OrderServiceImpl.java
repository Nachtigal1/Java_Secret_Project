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

    //TODO add Product quantity update after order

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
        List<OrderItem> orderItems = new ArrayList<>();

        if (cartResponseDTO != null) {
            for (CartResponseDTO.CartItemResponseDTO cartItem : cartResponseDTO.getCartItems()) {
                OrderItem orderItem = new OrderItem();
                orderItem.setProductId(cartItem.getProductId());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setPrice(cartItem.getPrice());

                try {
                    restTemplate.put(
                            productServiceUrl + "/internal/products/quantity/" + orderItem.getProductId() + "/" + orderItem.getQuantity(),
                            null,
                            Void.class
                    );
                    log.info("Product quantity updated");
                } catch (Exception ex) {
                    log.error("Failed to update product quantity", ex);
                }

                orderItems.add(orderItem);
            }
        }

        order.setOrderItems(orderItems);
        order.setUserId(userId);
        order.setStatus(Order.Status.PENDING);
        order.setDeliveryMethod(Order.DeliveryMethod.DELIVERY);
        order.setTotal(
                orderItems.stream()
                        .mapToDouble(orderItem -> orderItem.getPrice() * orderItem.getQuantity())
                        .sum()
        );

        log.info("Order created: {}", order);

        try {
            restTemplate.delete(
                    cartServiceUrl + "/internal/carts/clear/" + userId
            );
            log.info("Cart cleared for user: {}", userId);
        } catch (Exception ex) {
            log.error("Failed to clear cart for user: {}", userId, ex);
        }

        orderRepository.save(order);

        log.info("Order saved: {}", order);

        return getOrderResponseDTO(userId, order);
    }

    private static OrderResponseDTO getOrderResponseDTO(Long userId, Order order) {
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        List<OrderResponseDTO.OrderItemResponseDTO> orderItemResponseDTOList = getOrderItemResponseDTOS(order);

        orderResponseDTO.setId(order.getId());
        orderResponseDTO.setOrderItems(orderItemResponseDTOList);
        orderResponseDTO.setUserId(userId);
        orderResponseDTO.setStatus(order.getStatus().name());
        orderResponseDTO.setDeliveryMethod(order.getDeliveryMethod().name());
        orderResponseDTO.setTotal(order.getTotal());
        return orderResponseDTO;
    }

    private static List<OrderResponseDTO.OrderItemResponseDTO> getOrderItemResponseDTOS(Order order) {
        List<OrderResponseDTO.OrderItemResponseDTO> orderItemResponseDTOList = new ArrayList<>();

        if (order.getOrderItems() != null) {
            for (OrderItem item : order.getOrderItems()) {
                OrderResponseDTO.OrderItemResponseDTO orderItemResponseDTO = new OrderResponseDTO.OrderItemResponseDTO();
                orderItemResponseDTO.setProductId(item.getProductId());
                orderItemResponseDTO.setQuantity(item.getQuantity());
                orderItemResponseDTO.setPrice(item.getPrice());
                orderItemResponseDTOList.add(orderItemResponseDTO);
            }
        }
        return orderItemResponseDTOList;
    }
}
