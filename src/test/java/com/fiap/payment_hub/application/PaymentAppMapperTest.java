package com.fiap.payment_hub.application;

import com.fiap.payment_hub.application.dto.response.PaymentResponse;
import com.fiap.payment_hub.application.mappers.PaymentAppMapper;
import com.fiap.payment_hub.domain.entities.Payment;
import com.fiap.payment_hub.domain.enums.CardType;
import com.fiap.payment_hub.domain.enums.PaymentMethod;
import com.fiap.payment_hub.domain.enums.PaymentStatus;
import com.fiap.payment_hub.domain.valueobjects.Card;
import com.fiap.payment_hub.domain.valueobjects.Pix;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PaymentAppMapperTest {

    @Test
    void deveRetornarNuloQuandoOPagamentoForNulo() {

        PaymentResponse response = PaymentAppMapper.domainToResponse(null);

        assertNull(response);
    }

    @Test
    void deveMapearPagamentoComCartao() {

        UUID id = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime processedAt = createdAt.plusMinutes(1);

        Card card = new Card(
                "Roronoa Zoro",
                "1234567812345678",
                "12/30",
                "123",
                CardType.CREDIT
        );

        Payment payment = Payment.reconstitute(
                id,
                new BigDecimal("15000.00"),
                customerId,
                "Compra notebook",
                PaymentMethod.CARD,
                PaymentStatus.SUCCESS,
                card,
                null,
                createdAt,
                processedAt
        );

        PaymentResponse response = PaymentAppMapper.domainToResponse(payment);

        assertNotNull(response);

        assertEquals(id, response.id());
        assertEquals(customerId, response.customerId());
        assertEquals(new BigDecimal("15000.00"), response.amount());
        assertEquals("Compra notebook", response.description());
        assertEquals(PaymentMethod.CARD, response.paymentMethod());
        assertEquals(PaymentStatus.SUCCESS, response.status());

        assertNotNull(response.card());
        assertEquals("Roronoa Zoro", response.card().holderName());
        assertEquals("**** **** **** 5678", response.card().number());
        assertEquals("12/30", response.card().expiration());
        assertEquals(CardType.CREDIT, response.card().type());

        assertNull(response.pix());

        assertEquals(createdAt, response.createdAt());
        assertEquals(processedAt, response.processedAt());
    }

    @Test
    void deveMapearPagamentoComPix() {

        UUID id = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        Pix pix = new Pix(
                "frieren@email.com",
                LocalDateTime.now().plusMinutes(30)
        );

        Payment payment = Payment.reconstitute(
                id,
                new BigDecimal("200.00"),
                customerId,
                "Pagamento PIX",
                PaymentMethod.PIX,
                PaymentStatus.PENDING,
                null,
                pix,
                LocalDateTime.now(),
                null
        );

        PaymentResponse response = PaymentAppMapper.domainToResponse(payment);

        assertNotNull(response);

        assertNull(response.card());

        assertNotNull(response.pix());
        assertEquals("frieren@email.com", response.pix().key());
        assertEquals(pix.getExpiration(), response.pix().expiration());
    }

    @Test
    void deveMascararNumeroDoCartao() {

        Card card = new Card(
                "Toji Fushiguro",
                "1111222233334444",
                "10/30",
                "123",
                CardType.DEBIT
        );

        Payment payment = Payment.reconstitute(
                UUID.randomUUID(),
                BigDecimal.TEN,
                UUID.randomUUID(),
                "Teste",
                PaymentMethod.CARD,
                PaymentStatus.SUCCESS,
                card,
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        PaymentResponse response = PaymentAppMapper.domainToResponse(payment);

        assertEquals("**** **** **** 4444", response.card().number());
    }

    @Test
    void deveRetornarApenasAsteriscosQuandoONúmeroDoCartaoForNulo() {

        Card card = new Card(
                "Wang Ling",
                null,
                "10/30",
                "123",
                CardType.DEBIT
        );

        Payment payment = Payment.reconstitute(
                UUID.randomUUID(),
                BigDecimal.TEN,
                UUID.randomUUID(),
                "Teste",
                PaymentMethod.CARD,
                PaymentStatus.SUCCESS,
                card,
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        PaymentResponse response = PaymentAppMapper.domainToResponse(payment);

        assertEquals("****", response.card().number());
    }

    @Test
    void deveRetornarApenasAsteriscosQuandoONúmeroDoCartaoTiverMenosDeQuatroDigitos() {

        Card card = new Card(
                "Satoru Gojo",
                "123",
                "10/30",
                "123",
                CardType.DEBIT
        );

        Payment payment = Payment.reconstitute(
                UUID.randomUUID(),
                BigDecimal.TEN,
                UUID.randomUUID(),
                "Teste",
                PaymentMethod.CARD,
                PaymentStatus.SUCCESS,
                card,
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        PaymentResponse response = PaymentAppMapper.domainToResponse(payment);

        assertEquals("****", response.card().number());
    }
}
