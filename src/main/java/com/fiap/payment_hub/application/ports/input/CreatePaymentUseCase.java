package com.fiap.payment_hub.application.ports.input;

import com.fiap.payment_hub.application.dto.response.PaymentResponse;
import com.fiap.payment_hub.infrastructure.adapters.input.dto.request.PaymentInput;


public interface CreatePaymentUseCase {

    PaymentResponse execute(PaymentInput request);
}
