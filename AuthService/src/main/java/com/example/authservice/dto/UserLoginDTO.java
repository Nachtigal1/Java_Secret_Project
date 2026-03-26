package com.example.authservice.dto;

import lombok.*;

@Getter
@Setter
public class UserLoginDTO {

    @NonNull
    private String username;

    @NonNull
    private String password;
}
