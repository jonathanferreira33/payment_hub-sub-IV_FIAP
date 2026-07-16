package com.fiap.payment_hub.infrastructure.adapters.input.dto.request;

import com.fiap.payment_hub.domain.enums.PaymentMethod;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentInput(
        BigDecimal amount,
        UUID customerId,
        String description,
        UUID vendaId,
        UUID veiculoId,
        PaymentMethod paymentMethod,
        CardInput card,
        PixInput pix,
        String paymentCode
) {}