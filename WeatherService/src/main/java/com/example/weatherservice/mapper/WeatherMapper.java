package com.example.weatherservice.mapper;

import com.example.weatherservice.dto.*;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class WeatherMapper {

    public WeatherDTO mapStormglassToDto(StormglassResponse response) {

        if (response.getHours() == null || response.getHours().isEmpty()) {
            return emptyDto();
        }

        StormglassResponse.Hour hour = response.getHours().get(0);

        double waveHeight = extractValue(hour.getWaveHeight());
        double windSpeed = extractValue(hour.getWindSpeed());
        double temperature = extractValue(hour.getWaterTemperature());

        Double windDeg = extractNullable(hour.getWindDirection());
        String windDirection = mapWindDirection(windDeg);

        return new WeatherDTO(
                waveHeight,
                windSpeed,
                windDirection,
                temperature
        );
    }

    public WeatherDTO mapOpenWeatherToDto(OpenWeatherResponse response) {
        return WeatherDTO.builder()
                .temperature(response.getMain().getTemp())
                .windSpeed(response.getWind().getSpeed())
                .windDirection(mapWindDirection(response.getWind().getDeg()))
                .build();
    }

    private String mapWindDirection(Double degrees) {
        if (degrees == null) return "unknown";

        if (degrees >= 337.5 || degrees < 22.5) return "N";
        if (degrees < 67.5) return "NE";
        if (degrees < 112.5) return "E";
        if (degrees < 157.5) return "SE";
        if (degrees < 202.5) return "S";
        if (degrees < 247.5) return "SW";
        if (degrees < 292.5) return "W";
        return "NW";
    }

    private double extractValue(Map<String, Double> data) {
        if (data == null || data.isEmpty()) {
            return 0.0;
        }
        return data.getOrDefault("noaa", 0.0);
    }

    private WeatherDTO emptyDto() {
        return new WeatherDTO(
                0.0,
                0.0,
                "unknown",
                0.0
        );
    }

    private Double extractNullable(Map<String, Double> data) {
        if (data == null || data.isEmpty()) {
            return null;
        }
        return data.get("noaa");
    }
}
