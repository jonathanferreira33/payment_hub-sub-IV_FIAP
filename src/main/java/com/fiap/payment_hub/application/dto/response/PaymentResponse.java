package com.fiap.payment_hub.application.dto.response;

import com.fiap.payment_hub.domain.enums.PaymentMethod;
import com.fiap.payment_hub.domain.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;

import java.util.UUID;

public record PaymentResponse(
        UUID id,
        BigDecimal amount,
        UUID customerId,
        String description,
        PaymentMethod paymentMethod,
        PaymentStatus status,
        CardResponse card,
        PixResponse pix,
        Instant createdAt,
        Instant processedAt
) {}
