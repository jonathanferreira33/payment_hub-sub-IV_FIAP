package com.fiap.payment_hub.application.ports.output;

import com.fiap.payment_hub.domain.entities.Payment;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {

    Payment save(Payment payment);

    Optional<Payment> findById(UUID id);
}
