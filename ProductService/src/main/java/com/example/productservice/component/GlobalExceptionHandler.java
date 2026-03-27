package com.example.productservice.component;

import com.example.productservice.exception.*;
import com.example.productservice.dto.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({
            ProductNotFoundException.class,
            CategoryNotFoundException.class
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
            ProductAlreadyExistsException.class,
            CategoryAlreadyExistsException.class
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
