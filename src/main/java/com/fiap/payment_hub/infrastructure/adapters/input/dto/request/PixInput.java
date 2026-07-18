package com.fiap.payment_hub.infrastructure.adapters.input.dto.request;

import java.time.Instant;

public record PixInput(
        String key,
        Instant expiration
) {}