package com.example.weatherservice.service;

import com.example.weatherservice.dto.WeatherDTO;
import reactor.core.publisher.Mono;

public interface WeatherService {
    Mono<WeatherDTO> getWeather(double lat, double lng);
}
