package com.fiap.payment_hub.application.mappers;

import com.fiap.payment_hub.application.dto.response.CardResponse;
import com.fiap.payment_hub.application.dto.response.PaymentResponse;
import com.fiap.payment_hub.application.dto.response.PixResponse;
import com.fiap.payment_hub.domain.entities.Payment;
import com.fiap.payment_hub.domain.valueobjects.Card;
import com.fiap.payment_hub.domain.valueobjects.Pix;

public class PaymentAppMapper {

    public static PaymentResponse domainToResponse(Payment payment) {
        if (payment == null) {
            return null;
        }

        return new PaymentResponse(
                payment.getId(),
                payment.getAmount(),
                payment.getCustomerId(),
                payment.getDescription(),
                payment.getPaymentMethod(),
                payment.getStatus(),
                mapCardToResponse(payment.getCard()),
                mapPixToResponse(payment.getPix()),
                payment.getCreatedAt(),
                payment.getProcessedAt()
        );
    }

    private static CardResponse mapCardToResponse(Card card) {
        if (card == null) {
            return null;
        }

        return new CardResponse(
                card.getHolderName(),
                maskCardNumber(card.getNumber()),
                card.getExpiration(),
                card.getType()
        );
    }

    private static PixResponse mapPixToResponse(Pix pix) {
        if (pix == null) {
            return null;
        }

        return new PixResponse(
                pix.getKey(),
                pix.getExpiration()
        );
    }

    private static String maskCardNumber(String number) {
        if (number == null || number.length() < 4) {
            return "****";
        }
        String lastFour = number.substring(number.length() - 4);
        return "**** **** **** " + lastFour;
    }
}