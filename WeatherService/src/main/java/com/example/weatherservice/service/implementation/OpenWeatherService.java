package com.example.weatherservice.service.implementation;

import com.example.weatherservice.dto.*;
import com.example.weatherservice.mapper.WeatherMapper;
import com.example.weatherservice.service.WeatherService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OpenWeatherService implements WeatherService {

    private final WebClient webClient;
    private final WeatherMapper weatherMapper;

    @Override
    @CircuitBreaker(name = "weatherApi", fallbackMethod = "fallback")
    @Retry(name = "weatherApi")
    public Mono<SurfWeatherDTO> getWeather(double lat, double lng) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2/weather/point")
                        .queryParam("lat", lat)
                        .queryParam("lng", lng)
                        .queryParam("params", "waveHeight,windSpeed,waterTemperature,windDirection")
                        .build())
                .retrieve()
                .bodyToMono(StormglassResponse.class)
                .map(weatherMapper::mapToDto);
    }

    public Mono<SurfWeatherDTO> fallback(double lat, double lng, Throwable ex) {
        return Mono.just(new SurfWeatherDTO(
                0.0,
                0.0,
                "unknown",
                0.0
        ));
    }
}
