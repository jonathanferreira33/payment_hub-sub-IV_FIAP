package com.fiap.payment_hub.domain.exceptions;

public class InvalidPaymentException extends DomainException {

    public InvalidPaymentException(String message) {
        super(message);
    }

}