package com.fiap.payment_hub.application;

import com.fiap.payment_hub.application.dto.request.CardRequest;
import com.fiap.payment_hub.application.dto.request.PaymentRequest;
import com.fiap.payment_hub.application.dto.request.PixRequest;
import com.fiap.payment_hub.application.mappers.PaymentMapper;
import com.fiap.payment_hub.domain.enums.CardType;
import com.fiap.payment_hub.domain.enums.PaymentMethod;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PaymentIntegrationTest {

    @Test
    void deveConverterRequestParaInputComSucesso() {
        CardRequest cardRequest = new CardRequest(
                "Sung Jinwoo",
                "4111111111111111",
                "12/30",
                "123",
                CardType.CREDIT
        );

        PaymentRequest paymentRequest = new PaymentRequest(
                new BigDecimal("100.00"),
                UUID.randomUUID(),
                "Pagamento via Cartão",
                UUID.randomUUID(),
                UUID.randomUUID(),
                PaymentMethod.CARD,
                cardRequest,
                null,
                "CODE-1234"
        );

        var paymentInput = PaymentMapper.toInput(paymentRequest);

        assertNotNull(paymentInput);
        assertEquals(paymentRequest.amount(), paymentInput.amount());
        assertEquals(paymentRequest.card().number(), paymentInput.card().number());
        assertEquals(paymentRequest.paymentMethod(), paymentInput.paymentMethod());
    }

    @Test
    void deveConverterRequestPixParaInput() {
        PixRequest pixRequest = new PixRequest(
                "chave-pix-teste",
                Instant.now().plusSeconds(3600)
        );

        PaymentRequest paymentRequest = new PaymentRequest(
                new BigDecimal("50.00"),
                UUID.randomUUID(),
                "Pagamento via PIX",
                UUID.randomUUID(),
                UUID.randomUUID(),
                PaymentMethod.PIX,
                null,
                pixRequest,
                "CODE-5678"
        );

        var paymentInput = PaymentMapper.toInput(paymentRequest);

        assertNotNull(paymentInput.pix());
        assertEquals(pixRequest.key(), paymentInput.pix().key());
        assertNull(paymentInput.card());
    }
}