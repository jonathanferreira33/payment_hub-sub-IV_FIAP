package com.fiap.payment_hub.application.ports.input;

import com.fiap.payment_hub.application.dto.response.PaymentResponse;

import java.util.UUID;

public interface FindPaymentByIdUseCase {
    PaymentResponse execute(UUID id);
}
