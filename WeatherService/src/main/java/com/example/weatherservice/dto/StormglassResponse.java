package com.example.weatherservice.dto;

import lombok.Data;

import java.util.*;

@Data
public class StormglassResponse {

    private List<Hour> hours;

    @Data
    public static class Hour {
        private Map<String, Double> waveHeight;
        private Map<String, Double> windSpeed;
        private Map<String, Double> waterTemperature;
        private Map<String, Double> windDirection;
    }
}
