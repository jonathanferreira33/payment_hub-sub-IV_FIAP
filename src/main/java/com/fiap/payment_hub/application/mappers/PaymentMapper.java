package com.fiap.payment_hub.application.mappers;

import com.fiap.payment_hub.domain.enums.PaymentStatus;
import com.fiap.payment_hub.shared.enums.StatusPagamento;

public class PaymentMapper {
    public static StatusPagamento toWebhookStatus(PaymentStatus status) {
        return switch (status) {
            case SUCCESS -> StatusPagamento.CONFIRMADO;
            case FAILED, REJECTED, CANCELLED -> StatusPagamento.CANCELADO;
            default -> StatusPagamento.PENDENTE;
        };
    }
}
