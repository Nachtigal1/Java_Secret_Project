package com.example.orderservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@ToString
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "delivery_method", nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryMethod deliveryMethod;

    @OneToMany(mappedBy = "order", cascade =  CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Column(name = "total", nullable = false)
    private Double total = 0.0;

    public enum Status {
        PENDING, SHIPPED, DELIVERED, RETURNED;
    }

    public enum DeliveryMethod {
        DELIVERY, PICKUP;
    }
}
