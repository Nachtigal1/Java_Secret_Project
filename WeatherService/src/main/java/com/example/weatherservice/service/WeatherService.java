package com.example.weatherservice.service;

import reactor.core.publisher.Mono;

public interface WeatherService {

    Mono<SurfWeatherDTO> getWeather (Double lat, Double lng);

    Mono<SurfWeatherDTO> fallback (Double lat, Double lng, Throwable ex);
}
