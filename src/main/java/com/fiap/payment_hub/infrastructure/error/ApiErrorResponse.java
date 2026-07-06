package com.fiap.payment_hub.infrastructure.error;

public record ApiErrorResponse(
        String timestamp,
        int status,
        String error,
        String message,
        String path
) {
    public String getTitle() {
        return error;
    }
}