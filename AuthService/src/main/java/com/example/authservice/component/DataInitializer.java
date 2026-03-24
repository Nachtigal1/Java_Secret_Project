package com.example.authservice.component;

import com.example.authservice.model.User;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.service.implementation.JwtUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public void run(String... args) throws Exception {
        try {
            initAdmin();
        } catch (Exception e) {
            log.error("Failed to initialize admin", e);
        }
    }

    private void initAdmin() {
        if (userRepository.existsByUsername("admin")) {
            String adminToken = jwtUtil.generateToken(userRepository.findByUsername("admin").get().getId().toString());
            log.info("Admin Token: {}", adminToken);
            return;
        }
        
        User admin = new User();
        admin.setRole(User.UserRole.ADMIN);
        admin.setUsername("admin");
        admin.setPasswordHash(passwordEncoder.encode("admin"));

        userRepository.save(admin);

        String adminToken = jwtUtil.generateToken(admin.getId().toString());
        log.info("Admin Token: {}", adminToken);
    }
}
