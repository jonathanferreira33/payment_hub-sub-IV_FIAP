package com.fiap.payment_hub.application.dto.request;

import com.fiap.payment_hub.domain.enums.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        String customerId,
        String description,
        PaymentMethod paymentMethod,
        CardRequest card,
        PixRequest pix
) {}
