package com.example.weatherservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${stormglass.api-key}")
    private String apiKey;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("https://api.stormglass.io")
                .defaultHeader("Authorization", apiKey)
                .build();
    }
}
