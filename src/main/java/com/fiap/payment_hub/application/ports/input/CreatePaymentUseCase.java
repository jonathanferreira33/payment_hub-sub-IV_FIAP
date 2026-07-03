package com.fiap.payment_hub.application.ports.input;

import com.fiap.payment_hub.application.dto.request.PaymentRequest;
import com.fiap.payment_hub.application.dto.response.PaymentResponse;


public interface CreatePaymentUseCase {

    PaymentResponse execute(PaymentRequest request);
}
