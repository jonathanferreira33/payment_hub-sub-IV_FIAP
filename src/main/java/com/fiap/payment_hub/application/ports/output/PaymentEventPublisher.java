package com.fiap.payment_hub.application.ports.output;

import com.fiap.payment_hub.domain.entities.Payment;

public interface PaymentEventPublisher {

    void publishPaymentCreated(Payment paymentId);
}
