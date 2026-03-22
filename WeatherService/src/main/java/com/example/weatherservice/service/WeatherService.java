package com.example.weatherservice.service;

import com.example.weatherservice.dto.SurfWeatherDTO;
import reactor.core.publisher.Mono;

public interface WeatherService {
    Mono<SurfWeatherDTO> getWeather(double lat, double lng);
}
