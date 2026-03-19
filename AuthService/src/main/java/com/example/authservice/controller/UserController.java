package com.example.authservice.controller;

import com.example.authservice.dto.*;
import com.example.authservice.service.implementation.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
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

    @GetMapping
    public ResponseEntity<UserResponseDTO> getUserById (
            @RequestHeader("X-User") String id
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserById(Long.valueOf(id)));
    }
}
