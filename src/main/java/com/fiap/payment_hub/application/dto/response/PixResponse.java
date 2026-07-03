package com.fiap.payment_hub.application.dto.response;

import java.time.LocalDateTime;

public record PixResponse(
        String key,
        LocalDateTime expiration
) {}
