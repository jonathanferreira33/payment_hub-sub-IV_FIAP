package com.fiap.payment_hub.infrastructure.adapters.output.messaging;

import com.fiap.payment_hub.domain.entities.Payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentCreatedEvent(
        UUID paymentId,
        BigDecimal amount,
        String customerId,
        String paymentMethod,
        LocalDateTime createdAt
) {
    public static PaymentCreatedEvent fromDomain(Payment payment) {
        return new PaymentCreatedEvent(
                payment.getId(),
                payment.getAmount(),
                payment.getCustomerId(),
                payment.getPaymentMethod().name(),
                payment.getCreatedAt()
        );
    }
}
