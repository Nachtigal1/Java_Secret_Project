package com.example.cartservice.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse {

    private int status;

    private String message;

    private LocalDateTime timestamp;
}
