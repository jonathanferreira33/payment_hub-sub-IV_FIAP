package com.fiap.payment_hub.application.dto.request;

import java.time.LocalDateTime;

public record PixRequest(
        String key,
        LocalDateTime expiration
) {}
