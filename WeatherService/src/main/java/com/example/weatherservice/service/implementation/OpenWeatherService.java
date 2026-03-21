package com.example.weatherservice.service.implementation;

import com.example.weatherservice.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OpenWeatherService implements WeatherService {

    private final WebClient webClient;

    @Override
    public Mono<SurfWeatherDTO> getWeather(Double lat, Double lng) {
        return null;
    }

    @Override
    public Mono<SurfWeatherDTO> fallback(Double lat, Double lng, Throwable ex) {
        return null;
    }
}
