package com.fiap.payment_hub.application.dto.request;

import com.fiap.payment_hub.shared.enums.StatusPagamento;

import java.math.BigDecimal;
import java.util.UUID;

public record WebhookPagamentoRequest(
        UUID vendaId,
        BigDecimal valor,
        String codigoPagamento,
        StatusPagamento statusPagamento
) {
}