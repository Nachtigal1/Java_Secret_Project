package com.example.weatherservice.controller;

import com.example.weatherservice.dto.SurfWeatherDTO;
import com.example.weatherservice.service.WeatherService;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
@Validated
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping
    public Mono<ResponseEntity<SurfWeatherDTO>> getWeather(
            @RequestParam
            @DecimalMin(value = "-90.0", message = "Latitude must be >= -90")
            @DecimalMax(value = "90.0", message = "Latitude must be <= 90")
            Double lat,

            @RequestParam
            @DecimalMin(value = "-180.0", message = "Longitude must be >= -180")
            @DecimalMax(value = "180.0", message = "Longitude must be <= 180")
            Double lng
    ) {

        return weatherService.getWeather(lat, lng)
                .map(ResponseEntity::ok);
    }
}
