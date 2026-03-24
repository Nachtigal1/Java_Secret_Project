package com.example.weatherservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class OpenWeatherResponse {

    private Main main;
    private Wind wind;
    private List<Weather> weather;

    @Data
    public static class Main {
        private Double temp;
    }

    @Data
    public static class Wind {
        private Double speed;
        private Double deg;
    }

    @Data
    public static class Weather {
        private String description;
    }
}
