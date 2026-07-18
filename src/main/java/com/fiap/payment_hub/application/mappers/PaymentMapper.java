package com.fiap.payment_hub.application.mappers;

import com.fiap.payment_hub.application.dto.request.PaymentRequest;
import com.fiap.payment_hub.domain.enums.PaymentStatus;
import com.fiap.payment_hub.infrastructure.adapters.input.dto.request.PaymentInput;
import com.fiap.payment_hub.shared.enums.StatusPagamento;

public class PaymentMapper {
    public static StatusPagamento toWebhookStatus(PaymentStatus status) {
        return switch (status) {
            case SUCCESS -> StatusPagamento.CONFIRMADO;
            case FAILED, REJECTED, CANCELLED -> StatusPagamento.CANCELADO;
            default -> StatusPagamento.PENDENTE;
        };
    }

    public static PaymentInput toInput(PaymentRequest request) {
        if (request == null) return null;

        return new PaymentInput(
                request.amount(),
                request.customerId(),
                request.description(),
                request.vendaId(),
                request.veiculoId(),
                request.paymentMethod(),
                CardMapper.toInput(request.card()),
                PixMapper.toInput(request.pix()),
                request.paymentCode()
        );
    }
}
