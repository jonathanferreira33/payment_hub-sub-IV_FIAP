package com.fiap.payment_hub.application.dto.response;

import com.fiap.payment_hub.domain.enums.CardType;

public record CardResponse(
        String holderName,
        String number,
        String expiration,
        CardType type
) {}
