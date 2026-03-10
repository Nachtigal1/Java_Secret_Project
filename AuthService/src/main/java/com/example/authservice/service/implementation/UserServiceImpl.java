package com.example.authservice.service.implementation;

import com.example.authservice.dto.*;
import com.example.authservice.exception.UserAlreadyExistsException;
import com.example.authservice.exception.UserNotFoundException;
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
    private final JwtUtil jwtUtil;

    @Override
    public UserResponseDTO register(UserRegistrationDTO registrationDTO) {
        if (userRepository.findByUsername(registrationDTO.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("Username already exists");
        }

        User user = new User();

        user.setUsername(registrationDTO.getUsername());
        user.setPasswordHash(passwordEncoder.encode(registrationDTO.getPassword()));
        user.setRole(User.UserRole.USER);

        userRepository.save(user);

        return new UserResponseDTO(user.getUsername(), user.getPasswordHash(), user.getRole().toString());
    }

    @Override
    public TokenResponse login(UserLoginDTO loginDTO) {
        User user = userRepository.findByUsername(loginDTO.getUsername()).orElseThrow(
                () -> new UserNotFoundException("User with username " + loginDTO.getUsername() + " not found")
        );

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        return new TokenResponse(jwtUtil.generateToken(user.getUsername()));
    }
}
