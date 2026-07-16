package com.fiap.payment_hub.application.dto.request;

import com.fiap.payment_hub.shared.enums.StatusPagamento;

import java.util.UUID;

public record WebhookPagamentoRequest(
        UUID vendaId,
        StatusPagamento statusPagamento
) {
}