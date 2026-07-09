package com.fiap.payment_hub.application;

import com.fiap.payment_hub.application.dto.request.CardRequest;
import com.fiap.payment_hub.application.dto.request.PaymentRequest;
import com.fiap.payment_hub.application.dto.request.PixRequest;
import com.fiap.payment_hub.application.dto.response.PaymentResponse;
import com.fiap.payment_hub.application.ports.output.PaymentEventPublisher;
import com.fiap.payment_hub.application.ports.output.PaymentGateway;
import com.fiap.payment_hub.application.ports.output.PaymentRepository;
import com.fiap.payment_hub.application.services.CreatePaymentService;
import com.fiap.payment_hub.domain.entities.Payment;
import com.fiap.payment_hub.domain.enums.CardType;
import com.fiap.payment_hub.domain.enums.PaymentMethod;
import com.fiap.payment_hub.domain.enums.PaymentStatus;
import com.fiap.payment_hub.domain.valueobjects.Card;
import com.fiap.payment_hub.domain.valueobjects.Pix;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreatePaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentEventPublisher eventPublisher;

    @Mock
    private PaymentGateway paymentGateway;

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
                null
        );
    }

    @Test
    void deveProcessarPagamentoComCartaoAprovado() {

        when(paymentRepository.save(any(Payment.class)))
                .thenReturn(payment);

        when(paymentGateway.process(any(Payment.class)))
                .thenReturn(PaymentStatus.ACCEPTED);

        PaymentResponse response = service.execute(paymentRequest);

        assertNotNull(response);
        assertEquals(PaymentStatus.SUCCESS, response.status());

        verify(paymentRepository, times(2)).save(any(Payment.class));
        verify(paymentGateway).process(any(Payment.class));
        verify(eventPublisher).publishPaymentCreated(any(Payment.class));
    }

    @Test
    void deveProcessarPagamentoQuandoGatewayReprovar() {

        when(paymentRepository.save(any(Payment.class)))
                .thenReturn(payment);

        when(paymentGateway.process(any(Payment.class)))
                .thenReturn(PaymentStatus.REJECTED);

        PaymentResponse response = service.execute(paymentRequest);

        assertEquals(PaymentStatus.FAILED, response.status());

        verify(paymentRepository, times(2)).save(any(Payment.class));
        verify(eventPublisher).publishPaymentCreated(any(Payment.class));
    }

    @Test
    void devePublicarEventoAposProcessarPagamento() {

        when(paymentRepository.save(any()))
                .thenReturn(payment);

        when(paymentGateway.process(any()))
                .thenReturn(PaymentStatus.ACCEPTED);

        service.execute(paymentRequest);

        verify(eventPublisher).publishPaymentCreated(any(Payment.class));
    }

    @Test
    void deveSalvarPagamentoAntesEDepoisDoProcessamento() {

        when(paymentRepository.save(any()))
                .thenReturn(payment);

        when(paymentGateway.process(any()))
                .thenReturn(PaymentStatus.ACCEPTED);

        service.execute(paymentRequest);

        verify(paymentRepository, times(2))
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
                LocalDateTime.now().plusMinutes(30)
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
                )
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