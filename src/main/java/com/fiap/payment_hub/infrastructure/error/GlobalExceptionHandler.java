package com.fiap.payment_hub.infrastructure.error;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<ApiErrorResponse> handleDomainValidation(Exception ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ApiErrorResponse errorBody = new ApiErrorResponse(
                Instant.now().toString(),
                status.value(),
                "Business Rule Violation",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(errorBody);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
        ex.printStackTrace();

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        ApiErrorResponse errorBody = new ApiErrorResponse(
                Instant.now().toString(),
                status.value(),
                "Erro do Servidor Interno: ",
                "Ocorreu um erro inesperado. Entre em contato com o suporte.",
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(errorBody);
    }
}
