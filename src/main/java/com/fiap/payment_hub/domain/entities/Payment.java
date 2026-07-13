package com.fiap.payment_hub.domain.entities;

import com.fiap.payment_hub.domain.enums.PaymentMethod;
import com.fiap.payment_hub.domain.enums.PaymentStatus;
import com.fiap.payment_hub.domain.exceptions.InvalidPaymentException;
import com.fiap.payment_hub.domain.validators.PaymentValidator;
import com.fiap.payment_hub.domain.valueobjects.Card;
import com.fiap.payment_hub.domain.valueobjects.Pix;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Payment {

    private UUID id;
    private BigDecimal amount;
    private UUID customerId;
    private String description;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private Card card;
    private Pix pix;
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;
    private String paymentCode;

    public Payment(UUID id,
                   BigDecimal amount,
                   UUID customerId,
                   String description,
                   PaymentMethod paymentMethod,
                   PaymentStatus status,
                   Card card,
                   Pix pix,
                   LocalDateTime createdAt,
                   LocalDateTime processedAt,
                   String paymentCode) {
        this.id = id;
        this.amount = amount;
        this.customerId = customerId;
        this.description = description;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.card = card;
        this.pix = pix;
        this.createdAt = createdAt;
        this.processedAt = processedAt;
        this.paymentCode = paymentCode;
    }

    public UUID getId() { return id; }
    public BigDecimal getAmount() { return amount; }
    public UUID getCustomerId() { return customerId; }
    public String getDescription() { return description; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public PaymentStatus getStatus() { return status; }
    public Card getCard() { return card; }
    public Pix getPix() { return pix; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getProcessedAt() { return processedAt; }
    public String getPaymentCode() { return paymentCode; }

    public static Payment create(
            BigDecimal amount,
            UUID customerId,
            String description,
            PaymentMethod paymentMethod,
            Card card,
            Pix pix,
            String paymentCode
    ) {
        Payment pendingPayment = new Payment(
                UUID.randomUUID(),
                amount,
                customerId,
                description,
                paymentMethod,
                PaymentStatus.PROCESSING,
                card,
                pix,
                LocalDateTime.now(),
                null,
                paymentCode
        );

        PaymentValidator.validate(pendingPayment);

        return pendingPayment;
    }

    public static Payment reconstitute(
            UUID id,
            BigDecimal amount,
            UUID customerId,
            String description,
            PaymentMethod paymentMethod,
            PaymentStatus status,
            Card card,
            Pix pix,
            LocalDateTime createdAt,
            LocalDateTime processedAt,
            String paymentCode
    ) {
        return new Payment(
                id,
                amount,
                customerId,
                description,
                paymentMethod,
                status,
                card,
                pix,
                createdAt,
                processedAt,
                paymentCode
        );
    }

    public void startProcessing() {
        validateStatusTransition(PaymentStatus.PROCESSING);
        this.status = PaymentStatus.PROCESSING;
    }

    public void approve() {
        validateStatusTransition(PaymentStatus.SUCCESS);
        this.status = PaymentStatus.SUCCESS;
        this.processedAt = LocalDateTime.now();
    }

    public void fail() {
        validateStatusTransition(PaymentStatus.FAILED);
        this.status = PaymentStatus.FAILED;
        this.processedAt = LocalDateTime.now();
    }

    public void cancel() {
        validateStatusTransition(PaymentStatus.CANCELLED);
        this.status = PaymentStatus.CANCELLED;
    }

    private void validateStatusTransition(PaymentStatus nextStatus) {
        if (!this.status.canTransitionTo(nextStatus)) {
            throw new InvalidPaymentException(
                    String.format("Transição com status inválido de %s para %s.", this.status, nextStatus)
            );
        }
    }
}
