package com.example.authservice.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class UserResponseDTO {

    private Long id;

    private String username;

    private String userRole;
}
