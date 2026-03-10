package com.example.authservice.service;

import com.example.authservice.dto.UserRegistrationDTO;
import com.example.authservice.dto.UserResponseDTO;

public interface UserService {

    UserResponseDTO register (UserRegistrationDTO registrationDTO);
}
