package com.example.authservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password_hash", unique = true)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    public enum UserRole{
        USER, ADMIN
    }
}
