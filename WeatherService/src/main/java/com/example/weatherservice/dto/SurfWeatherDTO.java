package com.example.weatherservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SurfWeatherDTO {

    private double waveHeight;
    private double windSpeed;
    private String windDirection;
    private double temperature;
}
