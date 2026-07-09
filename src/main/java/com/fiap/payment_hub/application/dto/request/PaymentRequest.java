package com.fiap.payment_hub.application.dto.request;

import com.fiap.payment_hub.domain.enums.PaymentMethod;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentRequest(
        BigDecimal amount,
        UUID customerId,
        String description,
        UUID vendaId,
        PaymentMethod paymentMethod,
        CardRequest card,
        PixRequest pix,
        String codigoExternoPagamento
) {}
