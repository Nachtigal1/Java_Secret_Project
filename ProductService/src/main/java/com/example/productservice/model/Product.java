package com.example.productservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Product {

    @Id @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name",nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price", nullable = false)
    private Double price = 0.0;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "image", columnDefinition = "TEXT", nullable = false)
    private String image;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
