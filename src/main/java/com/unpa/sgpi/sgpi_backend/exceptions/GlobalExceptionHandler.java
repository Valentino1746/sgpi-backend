package com.unpa.sgpi.sgpi_backend.exceptions;

import com.unpa.sgpi.sgpi_backend.dto.ErrorResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadCredentials(BadCredentialsException ex) {
        return build(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas", null);
    }

    @ExceptionHandler({org.springframework.security.access.AccessDeniedException.class, AuthorizationDeniedException.class})
    public ResponseEntity<ErrorResponseDTO> handleAccessDenied(Exception ex) {
        return build(HttpStatus.FORBIDDEN, "Acceso denegado", null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        error -> error.getField(),
                        error -> error.getDefaultMessage() == null ? "Valor inválido" : error.getDefaultMessage(),
                        (first, second) -> first
                ));
        return build(HttpStatus.BAD_REQUEST, "Error de validación", errors);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFound(EntityNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    private ResponseEntity<ErrorResponseDTO> build(HttpStatus status, String message, Map<String, String> errors) {
        return ResponseEntity.status(status).body(new ErrorResponseDTO(
                status.value(),
                message,
                LocalDateTime.now(),
                errors
        ));
    }
}
