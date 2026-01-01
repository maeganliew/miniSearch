package com.minisearch.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // For validation errors
    // Spring throws this when mapping failed on DTOs
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationErrors(MethodArgumentNotValidException ex) {
        // getAllErrors returns a List of Object Error
        String errors = ex.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getDefaultMessage) // can use lambda here, but using "method references" is simpler
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest().body(errors);
    }

    // Handle malformed JSON / Invalid formats e.g. invalid date format
    @ExceptionHandler({JsonParseException.class, InvalidFormatException.class})
    public ResponseEntity<String> handleJsonExceptions(Exception ex) {
        String message;

        if (ex instanceof JsonParseException) {
            message = "Malformed JSON: " + ex.getMessage();
        } else if (ex instanceof InvalidFormatException invalidEx) {
            String field = invalidEx.getPath().getFirst().getFieldName();
            Object value = invalidEx.getValue();
            message = "Invalid value for field '" + field + "': " + value;
        } else {
            message = "JSON error: " + ex.getMessage();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    // For other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal server error: " + ex.getMessage());
    }
}