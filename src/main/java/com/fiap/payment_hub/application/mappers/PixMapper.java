package com.fiap.payment_hub.application.mappers;

import com.fiap.payment_hub.application.dto.request.PixRequest;
import com.fiap.payment_hub.infrastructure.adapters.input.dto.request.PixInput;

public class PixMapper {

    public static PixInput toInput(PixRequest request) {
        if (request == null) return null;

        return new PixInput(
                request.key(),
                request.expiration()
        );
    }
}