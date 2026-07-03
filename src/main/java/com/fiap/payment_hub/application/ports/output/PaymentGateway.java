package com.fiap.payment_hub.application.ports.output;

import com.fiap.payment_hub.domain.entities.Payment;
import com.fiap.payment_hub.domain.enums.PaymentStatus;

public interface PaymentGateway {

    PaymentStatus process(Payment payment);

}