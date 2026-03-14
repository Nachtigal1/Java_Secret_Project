package com.example.authservice.service.implementation;

import com.example.authservice.dto.*;
import com.example.authservice.exception.*;
import com.example.authservice.model.User;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RestTemplate restTemplate;

    @Value("${cart-service.url}")
    private String cartServiceUrl;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserResponseDTO register(UserRegistrationDTO registrationDTO) {
        if (userRepository.findByUsername(registrationDTO.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("Username already exists");
        }

        User user = new User();

        user.setUsername(registrationDTO.getUsername());
        user.setPasswordHash(passwordEncoder.encode(registrationDTO.getPassword()));
        user.setRole(User.UserRole.USER);

        userRepository.save(user);

        restTemplate.postForObject(
                cartServiceUrl + "/internal/carts",
                new CartCreateRequest(user.getId(), new ArrayList<>()),
                CartCreateRequest.class
        );

        return new UserResponseDTO(user.getId(), user.getUsername(), user.getRole().toString());
    }

    @Override
    public TokenResponse login(UserLoginDTO loginDTO) {
        User user = userRepository.findByUsername(loginDTO.getUsername()).orElseThrow(
                () -> new UserNotFoundException("User with username " + loginDTO.getUsername() + " not found")
        );

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        return new TokenResponse(jwtUtil.generateToken(user.getId().toString()));
    }
}
