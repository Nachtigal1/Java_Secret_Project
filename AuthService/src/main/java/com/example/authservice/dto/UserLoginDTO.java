package com.example.authservice.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class UserLoginDTO {

    @NonNull
    private String username;

    @NonNull
    private String password;
}
