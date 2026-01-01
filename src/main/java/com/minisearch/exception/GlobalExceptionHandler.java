package com.minisearch.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String message = "Invalid request body";

        Throwable cause = ex.getMostSpecificCause();

        if (cause instanceof JsonParseException) {
            message = "Malformed JSON: " + cause.getMessage();
        } else if (cause instanceof InvalidFormatException invalidEx) {
            // Identify the field and the invalid value
            String field = invalidEx.getPath().stream()
                    .map(JsonMappingException.Reference::getFieldName)
                    .reduce((first, second) -> second) // get the deepest field
                    .orElse("unknown");
            Object value = invalidEx.getValue();
            String targetType = invalidEx.getTargetType().getSimpleName();
            message = "Invalid value for field '" + field + "': '" + value + "'. Expected type: " + targetType;
        } else if (cause != null) {
            message = cause.getMessage();
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