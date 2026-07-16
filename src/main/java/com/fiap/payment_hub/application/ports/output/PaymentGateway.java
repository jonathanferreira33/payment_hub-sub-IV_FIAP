package com.fiap.payment_hub.application.ports.output;

import com.fiap.payment_hub.application.dto.request.WebhookPagamentoRequest;
import com.fiap.payment_hub.domain.entities.Payment;
import com.fiap.payment_hub.domain.enums.PaymentStatus;

import java.util.UUID;

public interface PaymentGateway {

    PaymentStatus process(Payment payment);
    void notifyStatus(UUID idVeiculo, WebhookPagamentoRequest request);

}