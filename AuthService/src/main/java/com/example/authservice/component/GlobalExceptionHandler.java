package com.example.authservice.component;

import com.example.authservice.dto.ExceptionResponse;
import com.example.authservice.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({
            UserNotFoundException.class
    })
    public ResponseEntity<ExceptionResponse> handleNotFoundExceptions(RuntimeException ex) {
        log.warn("Not found exception: {}", ex.getMessage());
        return buildExceptionResponse(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            DataIntegrityViolationException.class
    })
    public ResponseEntity<ExceptionResponse> handleDatabaseException(RuntimeException ex) {
        log.warn("Database exception: {}", ex.getMessage());
        return buildExceptionResponse(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            UserAlreadyExistsException.class
    })
    public ResponseEntity<ExceptionResponse> handleAlreadyExistsExceptions(RuntimeException ex) {
        log.warn("Already exists exception: {}", ex.getMessage());
        return buildExceptionResponse(ex, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGenericException(Exception ex) {
        log.error("Unhandled exception occurred: {}", ex.getMessage());
        return buildExceptionResponse(
                ex,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
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
