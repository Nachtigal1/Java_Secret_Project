package com.example.weatherservice.service.implementation;

import com.example.weatherservice.dto.*;
import com.example.weatherservice.mapper.WeatherMapper;
import com.example.weatherservice.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OpenWeatherService implements WeatherService {

    private final @Qualifier("openWeather") WebClient openWeatherWebClient;
    private final WeatherMapper weatherMapper;
    @Value("${openweather.api-key}")
    private String openWeatherApiKey;

    @Override
    public Mono<WeatherDTO> getWeather(double lat, double lng) {
        return openWeatherWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/data/2.5/weather")
                        .queryParam("lat", lat)
                        .queryParam("lon", lng)
                        .queryParam("appid", openWeatherApiKey)
                        .queryParam("units", "metric")
                        .build())
                .retrieve()
                .bodyToMono(OpenWeatherResponse.class)
                .map(weatherMapper::mapOpenWeatherToDto);
    }
}
