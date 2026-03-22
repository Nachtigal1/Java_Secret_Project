package com.example.cartservice.component;

import com.example.cartservice.dto.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import com.example.cartservice.exception.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({
            ProductNotFoundException.class,
            CartNotFoundException.class
    })
    public ResponseEntity<ExceptionResponse> handleNotFoundExceptions(RuntimeException ex) {
        log.warn("Not found exception: {}", ex.getMessage());
        return buildExceptionResponse(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            NotEnoughStockException.class
    })
    public ResponseEntity<ExceptionResponse> handleNotEnoughStockExceptions(RuntimeException ex) {
        log.warn("Not enough stock exception: {}", ex.getMessage());
        return buildExceptionResponse(ex, HttpStatus.CONFLICT);
    }

    private ResponseEntity<ExceptionResponse> buildExceptionResponse(Exception ex, HttpStatus status) {
        ExceptionResponse error = new ExceptionResponse(
                status.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, status);
    }
}
