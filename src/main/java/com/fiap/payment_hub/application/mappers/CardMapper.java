package com.fiap.payment_hub.application.mappers;

import com.fiap.payment_hub.application.dto.request.CardRequest;
import com.fiap.payment_hub.infrastructure.adapters.input.dto.request.CardInput;

public class CardMapper {

    public static CardInput toInput(CardRequest request) {
        if (request == null) return null;

        return new CardInput(
                request.holderName(),
                request.number(),
                request.expiration(),
                request.cvv(),
                request.type()
        );
    }
}