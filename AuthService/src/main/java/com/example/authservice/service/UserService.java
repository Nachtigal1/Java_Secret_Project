package com.example.authservice.service;

import com.example.authservice.dto.TokenResponse;
import com.example.authservice.dto.UserLoginDTO;
import com.example.authservice.dto.UserRegistrationDTO;
import com.example.authservice.dto.UserResponseDTO;

public interface UserService {

    UserResponseDTO register (UserRegistrationDTO registrationDTO);
    TokenResponse login (UserLoginDTO loginDTO);
    UserResponseDTO getUserById (Long id);

}
