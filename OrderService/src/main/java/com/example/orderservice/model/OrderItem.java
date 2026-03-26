package com.example.orderservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@ToString
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price", nullable = false)
    private Double price = 0.0;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
}
