package com.example.weatherservice.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${stormglass.api-key}")
    private String stormglassApiKey;

    @Value("${stormglass.url}")
    private String stormglassUrl;

    @Value("${openweather.url}")
    private String openWeatherUrl;

    @Bean
    @Qualifier("openWeather")
    public WebClient openWeatherWebClient() {
        return WebClient.builder()
                .baseUrl(openWeatherUrl)
                .build();
    }

    @Bean
    @Qualifier("stormglass")
    public WebClient stormglassWebClient() {
        return WebClient.builder()
                .baseUrl(stormglassUrl)
                .defaultHeader("Authorization", stormglassApiKey)
                .build();
    }
}
