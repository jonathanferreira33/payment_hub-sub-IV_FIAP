package com.fiap.payment_hub.application.dto.request;

import java.time.Instant;

public record PixRequest(
        String key,
        Instant expiration
) {}
