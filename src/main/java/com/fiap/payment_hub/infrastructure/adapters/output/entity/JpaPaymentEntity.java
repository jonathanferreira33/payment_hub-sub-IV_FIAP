package com.fiap.payment_hub.infrastructure.adapters.output.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_payments")
public class JpaPaymentEntity {

    @Id
    private UUID id;

    private BigDecimal amount;
    private UUID customerId;
    private String description;
    private String paymentMethod;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;

    private String cardHolderName;
    private String cardNumber;
    private String cardExpiration;
    private String cardType;

    private String pixKey;
    private LocalDateTime pixExpiration;

    protected JpaPaymentEntity() {}

    public JpaPaymentEntity(UUID id,
                            BigDecimal amount,
                            UUID customerId,
                            String description,
                            String paymentMethod,
                            String status,
                            LocalDateTime createdAt,
                            LocalDateTime processedAt,
                            String cardHolderName,
                            String cardNumber,
                            String cardExpiration,
                            String cardType,
                            String pixKey,
                            LocalDateTime pixExpiration) {
        this.id = id;
        this.amount = amount;
        this.customerId = customerId;
        this.description = description;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.createdAt = createdAt;
        this.processedAt = processedAt;
        this.cardHolderName = cardHolderName;
        this.cardNumber = cardNumber;
        this.cardExpiration = cardExpiration;
        this.cardType = cardType;
        this.pixKey = pixKey;
        this.pixExpiration = pixExpiration;
    }

    public UUID getId() { return id; }
    public BigDecimal getAmount() { return amount; }
    public UUID getCustomerId() { return customerId; }
    public String getDescription() { return description; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getProcessedAt() { return processedAt; }
    public String getCardHolderName() { return cardHolderName; }
    public String getCardNumber() { return cardNumber; }
    public String getCardExpiration() { return cardExpiration; }
    public String getCardType() { return cardType; }
    public String getPixKey() { return pixKey; }
    public LocalDateTime getPixExpiration() { return pixExpiration; }
}
