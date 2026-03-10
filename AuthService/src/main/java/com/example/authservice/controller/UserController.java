package com.example.authservice.controller;

import com.example.authservice.dto.TokenResponse;
import com.example.authservice.dto.UserLoginDTO;
import com.example.authservice.dto.UserRegistrationDTO;
import com.example.authservice.dto.UserResponseDTO;
import com.example.authservice.service.implementation.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerNewUser (
            @RequestBody UserRegistrationDTO userRegistrationDTO
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(userRegistrationDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> loginUser (
            @RequestBody UserLoginDTO userLoginDTO
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.login(userLoginDTO));
    }
}
