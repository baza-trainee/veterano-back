package com.zdoryk.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIncorrectDataError(IllegalStateException ex) {
        Map<String, String> errors = new HashMap<>();
        String errorMessage = ex.getMessage();
        errors.put("error", errorMessage);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errors);
    }

}
