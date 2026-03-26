package com.example.authservice.dto;

import lombok.*;

@Getter
@Setter
public class UserRegistrationDTO {

    @NonNull
    private String username;

    @NonNull
    private String password;
}
