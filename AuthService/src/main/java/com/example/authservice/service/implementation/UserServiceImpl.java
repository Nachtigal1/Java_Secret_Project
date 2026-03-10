package com.example.authservice.service.implementation;

import com.example.authservice.dto.UserRegistrationDTO;
import com.example.authservice.dto.UserResponseDTO;
import com.example.authservice.model.User;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDTO register(UserRegistrationDTO registrationDTO) {
        User user = new User();

        user.setUsername(registrationDTO.getUsername());
        user.setPasswordHash(passwordEncoder.encode(registrationDTO.getPassword()));

        userRepository.save(user);

        return new UserResponseDTO(user.getUsername(), user.getPasswordHash());
    }
}
