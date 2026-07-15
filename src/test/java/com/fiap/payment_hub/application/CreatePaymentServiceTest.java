package com.fiap.payment_hub.application;

import com.fiap.payment_hub.application.dto.request.CardRequest;
import com.fiap.payment_hub.application.dto.request.PaymentRequest;
import com.fiap.payment_hub.application.dto.request.PixRequest;
import com.fiap.payment_hub.application.dto.response.PaymentResponse;
import com.fiap.payment_hub.application.ports.output.PaymentGateway;
import com.fiap.payment_hub.application.ports.output.PaymentRepository;
import com.fiap.payment_hub.application.services.AsyncPaymentProcessor;
import com.fiap.payment_hub.application.services.CreatePaymentService;
import com.fiap.payment_hub.domain.entities.Payment;
import com.fiap.payment_hub.domain.enums.CardType;
import com.fiap.payment_hub.domain.enums.PaymentMethod;
import com.fiap.payment_hub.domain.enums.PaymentStatus;
import com.fiap.payment_hub.domain.valueobjects.Card;
import com.fiap.payment_hub.domain.valueobjects.Pix;
import com.fiap.payment_hub.infrastructure.error.PaymentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CreatePaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentGateway paymentGateway;

    @Mock
    private AsyncPaymentProcessor asyncProcessor;

    @InjectMocks
    private CreatePaymentService service;

    private PaymentRequest paymentRequest;
    private Payment payment;

    @BeforeEach
    void setup() {

        CardRequest card = new CardRequest(
                "Sung Jinwoo",
                "4111111111111111",
                "12/30",
                "123",
                CardType.CREDIT
        );

        paymentRequest = new PaymentRequest(
                new BigDecimal("100.00"),
                UUID.randomUUID(),
                "Pagamento teste",
                UUID.randomUUID(),
                PaymentMethod.CARD,
                card,
                null,
                "AAAA-1234"
        );

        payment = Payment.create(
                paymentRequest.amount(),
                paymentRequest.customerId(),
                paymentRequest.description(),
                paymentRequest.paymentMethod(),
                new Card(
                        "Sung Jinwoo",
                        "4111111111111111",
                        "12/30",
                        "123",
                        CardType.CREDIT
                ),
                null,
                "ABCD-1234"
        );
    }

    @Test
    void deveProcessarPagamentoComCartaoAprovado() {
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        PaymentResponse response = service.execute(paymentRequest);

        assertNotNull(response);
        assertEquals(PaymentStatus.PROCESSING, response.status());

        verify(asyncProcessor, times(1)).processAsynchronousPayment(any(UUID.class), any(UUID.class));
    }

    @Test
    void deveIniciarProcessamentoDePagamento() {

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        PaymentResponse response = service.execute(paymentRequest);

        assertEquals(PaymentStatus.PROCESSING, response.status());

        verify(paymentRepository, times(1)).save(any(Payment.class));

        verify(asyncProcessor, times(1)).processAsynchronousPayment(any(UUID.class), any(UUID.class));
    }

    @Test
    void devePublicarEventoAposProcessarPagamento() {

        when(paymentRepository.save(any()))
                .thenReturn(payment);

        when(paymentGateway.process(any()))
                .thenReturn(PaymentStatus.ACCEPTED);

        service.execute(paymentRequest);
    }

    @Test
    void deveSalvarPagamentoAntesEDepoisDoProcessamento() {

        when(paymentRepository.save(any()))
                .thenReturn(payment);

        when(paymentGateway.process(any()))
                .thenReturn(PaymentStatus.ACCEPTED);

        service.execute(paymentRequest);

        verify(paymentRepository, times(1))
                .save(any(Payment.class));
    }

    @Test
    void deveRetornarPaymentResponse() {

        when(paymentRepository.save(any()))
                .thenReturn(payment);

        when(paymentGateway.process(any()))
                .thenReturn(PaymentStatus.ACCEPTED);

        PaymentResponse response = service.execute(paymentRequest);

        assertNotNull(response);
        assertEquals(payment.getCustomerId(), response.customerId());
        assertEquals(payment.getAmount(), response.amount());
    }

    @Test
    void deveCriarPagamentoSemCartaoQuandoPixForInformado() {

        PixRequest pix = new PixRequest(
                "11999999999",
                Instant.now().plus(30, ChronoUnit.MINUTES)
        );

        PaymentRequest request = new PaymentRequest(
                new BigDecimal("100.00"),
                UUID.randomUUID(),
                "Pagamento teste",
                UUID.randomUUID(),
                PaymentMethod.PIX,
                null,
                pix,
                "AAAA-1234"
        );

        Payment pixPayment = Payment.create(
                request.amount(),
                request.customerId(),
                request.description(),
                request.paymentMethod(),
                null,
                new Pix(
                        pix.key(),
                        pix.expiration()
                ),
                "ABCD-1234"
        );

        when(paymentRepository.save(any()))
                .thenReturn(pixPayment);

        when(paymentGateway.process(any()))
                .thenReturn(PaymentStatus.ACCEPTED);

        PaymentResponse response = service.execute(request);

        assertNotNull(response.pix());
        assertNull(response.card());
    }

}
