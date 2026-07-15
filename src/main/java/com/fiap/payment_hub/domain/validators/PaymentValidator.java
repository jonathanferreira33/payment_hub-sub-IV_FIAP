package com.fiap.payment_hub.domain.validators;

import com.fiap.payment_hub.domain.entities.Payment;
import com.fiap.payment_hub.domain.exceptions.InvalidPaymentException;
import com.fiap.payment_hub.domain.valueobjects.Card;
import com.fiap.payment_hub.domain.valueobjects.Pix;

import java.math.BigDecimal;
import java.util.UUID;

public class PaymentValidator {

    public static void validate(Payment payment) {
        if (payment == null) {
            throw new InvalidPaymentException("O pagamento não pode ser nulo.");
        }

        validateAmount(payment.getAmount());
        validateCustomerId(payment.getCustomerId());
        validatePaymentMethodSpecifics(payment);
    }

    private static void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidPaymentException("O valor do pagamento deve ser maior que zero.");
        }
    }

    private static void validateCustomerId(UUID customerId) {
        if (customerId == null) {
            throw new InvalidPaymentException("O ID do cliente é obrigatório.");
        }
    }

    private static void validatePaymentMethodSpecifics(Payment payment) {
        if (payment.getPaymentMethod() == null) {
            throw new InvalidPaymentException("É necessário informar uma forma de pagamento.");
        }

        switch (payment.getPaymentMethod()) {
            case PIX -> validatePixSpecifics(payment.getPix());
            case CARD -> validateCardSpecifics(payment.getCard());
        }
    }

    private static void validatePixSpecifics(Pix pix) {
        if (pix == null) {
            throw new InvalidPaymentException("Dados do Pix são necessários para o método de pagamento Pix.");
        }
    }

    private static void validateCardSpecifics(Card card) {
        if (card == null) {
            throw new InvalidPaymentException("Os dados do cartão são necessários para o método de pagamento com cartão.");
        }
        if (card.getNumber() == null || card.getNumber().trim().isEmpty()) {
            throw new InvalidPaymentException("O número do cartão é obrigatório.");
        }
    }
}
