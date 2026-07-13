package com.fiap.payment_hub.infrastructure.adapters.output.mapper;

import com.fiap.payment_hub.domain.entities.Payment;
import com.fiap.payment_hub.domain.enums.CardType;
import com.fiap.payment_hub.domain.enums.PaymentMethod;
import com.fiap.payment_hub.domain.enums.PaymentStatus;
import com.fiap.payment_hub.domain.valueobjects.Card;
import com.fiap.payment_hub.domain.valueobjects.Pix;
import com.fiap.payment_hub.infrastructure.adapters.output.entity.JpaPaymentEntity;

import java.time.LocalDateTime;

public class PaymentInfrastructureMapper {

    public static JpaPaymentEntity domainToJpa(Payment domain) {
        if (domain == null) return null;

        String cardHolder = domain.getCard() != null ? domain.getCard().getHolderName() : null;
        String cardNumber = domain.getCard() != null ? domain.getCard().getNumber() : null;
        String cardExp = domain.getCard() != null ? domain.getCard().getExpiration() : null;
        String cardType = domain.getCard() != null && domain.getCard().getType() != null ? domain.getCard().getType().name() : null;

        String pixKey = domain.getPix() != null ? domain.getPix().getKey() : null;
        LocalDateTime pixExp = domain.getPix() != null ? domain.getPix().getExpiration() : null;

        return new JpaPaymentEntity(
                domain.getId(),
                domain.getAmount(),
                domain.getCustomerId(),
                domain.getDescription(),
                domain.getPaymentMethod().name(),
                domain.getStatus().name(),
                domain.getCreatedAt(),
                domain.getProcessedAt(),
                cardHolder, cardNumber, cardExp, cardType,
                pixKey, pixExp, domain.getPaymentCode()
        );
    }

    public static Payment jpaToDomain(JpaPaymentEntity jpa) {
        if (jpa == null) return null;

        Card card = null;
        if (jpa.getCardNumber() != null) {
            CardType type = jpa.getCardType() != null ? CardType.valueOf(jpa.getCardType()) : null;
            card = new Card(jpa.getCardHolderName(), jpa.getCardNumber(), jpa.getCardExpiration(), null, type); // CVV não é persistido por segurança
        }

        Pix pix = null;
        if (jpa.getPixKey() != null) {
            pix = new Pix(jpa.getPixKey(), jpa.getPixExpiration());
        }

        return Payment.reconstitute(
                jpa.getId(),
                jpa.getAmount(),
                jpa.getCustomerId(),
                jpa.getDescription(),
                PaymentMethod.valueOf(jpa.getPaymentMethod()),
                PaymentStatus.valueOf(jpa.getStatus()),
                card,
                pix,
                jpa.getCreatedAt(),
                jpa.getProcessedAt(),
                jpa.getPaymentCode()
        );
    }
}