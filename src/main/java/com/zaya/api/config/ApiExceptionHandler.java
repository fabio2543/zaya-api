package com.zaya.api.config;

import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
    return ResponseEntity.badRequest().body(Map.of(
        "error", "invalid_request",
        "message", ex.getMessage()
    ));
  }

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<Map<String, Object>> handleNotFound(NoSuchElementException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
        "error", "not_found",
        "message", ex.getMessage()
    ));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
    var errors = new ArrayList<Map<String, String>>();
    ex.getBindingResult().getFieldErrors().forEach(f ->
        errors.add(Map.of("field", f.getField(), "message", f.getDefaultMessage()))
    );
    return ResponseEntity.badRequest().body(Map.of(
        "error", "validation_error",
        "details", errors
    ));
  }
}