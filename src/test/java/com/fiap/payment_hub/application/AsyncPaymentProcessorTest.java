package com.fiap.payment_hub.application;

import com.fiap.payment_hub.application.ports.output.PaymentGateway;
import com.fiap.payment_hub.application.ports.output.PaymentRepository;
import com.fiap.payment_hub.application.services.AsyncPaymentProcessor;
import com.fiap.payment_hub.domain.entities.Payment;
import com.fiap.payment_hub.domain.enums.PaymentMethod;
import com.fiap.payment_hub.domain.enums.PaymentStatus;
import com.fiap.payment_hub.domain.valueobjects.Pix;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsyncPaymentProcessorTest {

    @Mock
    private PaymentRepository repository;

    @Mock
    private PaymentGateway paymentGateway;

    @InjectMocks
    private AsyncPaymentProcessor asyncProcessor;

    @Test
    void deveNotificarGatewayAposProcessamento() {

        UUID idPagamento = UUID.randomUUID();

        Payment payment = Payment.create(
                BigDecimal.valueOf(100),
                UUID.randomUUID(),
                "Teste",
                PaymentMethod.PIX,
                null,
                new Pix("chave", Instant.now().plusSeconds(3600)),
                "ABCD-1234"
        );

        when(repository.findById(idPagamento))
                .thenReturn(Optional.of(payment));

        when(paymentGateway.process(payment))
                .thenReturn(PaymentStatus.ACCEPTED);

        asyncProcessor.processAsynchronousPayment(idPagamento);

        verify(paymentGateway).process(payment);

        verify(paymentGateway).notifyStatus(
                eq("ABCD-1234"),
                eq("ACCEPTED")
        );
    }
}
