package com.fiap.payment_hub.application.dto.response;

import java.time.Instant;

public record PixResponse(
        String key,
        Instant expiration
) {}
