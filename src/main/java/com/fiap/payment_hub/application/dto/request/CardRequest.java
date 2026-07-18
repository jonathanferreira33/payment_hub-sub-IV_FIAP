package com.fiap.payment_hub.application.dto.request;

import com.fiap.payment_hub.domain.enums.CardType;

public record CardRequest(
        String holderName,
        String number,
        String expiration,
        String cvv,
        CardType type
) {}
