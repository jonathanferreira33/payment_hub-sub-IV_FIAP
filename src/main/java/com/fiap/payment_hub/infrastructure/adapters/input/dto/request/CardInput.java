package com.fiap.payment_hub.infrastructure.adapters.input.dto.request;

import com.fiap.payment_hub.domain.enums.CardType;

public record CardInput(
        String holderName,
        String number,
        String expiration,
        String cvv,
        CardType type
) {}