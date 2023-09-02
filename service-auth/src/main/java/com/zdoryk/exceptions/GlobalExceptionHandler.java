package com.zdoryk.exceptions;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.zdoryk.core.APICustomResponse;
import com.zdoryk.core.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserValidationException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(UserValidationException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error -> {
            String field = error.getField();
            String message = error.getDefaultMessage();
            errors.put(field, message);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(ResourceExistsException.class)
    public ResponseEntity<Map<String, String>> handleResourceExistsException(ResourceExistsException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errors);
    }

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<Map<String, String>> handleRateLimitException(RateLimitExceededException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(errors);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleServerError(RuntimeException ex) {
        Map<String, String> errors = new HashMap<>();
        String errorMessage = "We are experiencing temporary server issues. Please try again later.";
        errors.put("error",errorMessage);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errors);
    }


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFoundError(NotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        String errorMessage = ex.getMessage();
        errors.put("error",errorMessage);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }

    @ExceptionHandler(IncorrectDataException.class)
    public ResponseEntity<Map<String, String>> handleIncorrectDataError(IncorrectDataException ex) {
        Map<String, String> errors = new HashMap<>();
        String errorMessage = ex.getMessage();
        errors.put("error", errorMessage);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errors);
    }

    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<Map<String, String>> handleIncorrectDataError(JWTVerificationException ex) {
        Map<String, String> errors = new HashMap<>();
        String errorMessage = ex.getMessage();
        errors.put("error", errorMessage);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errors);
    }


}




