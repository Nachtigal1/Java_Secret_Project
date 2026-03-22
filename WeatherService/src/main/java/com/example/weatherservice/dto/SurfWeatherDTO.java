package com.example.weatherservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SurfWeatherDTO {

    private double waveHeight;
    private double windSpeed;
    private String windDirection;
    private double temperature;
}
