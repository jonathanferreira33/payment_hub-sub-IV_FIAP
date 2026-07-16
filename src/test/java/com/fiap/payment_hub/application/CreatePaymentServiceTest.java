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
import com.fiap.payment_hub.infrastructure.adapters.input.dto.request.CardInput;
import com.fiap.payment_hub.infrastructure.adapters.input.dto.request.PaymentInput;
import com.fiap.payment_hub.infrastructure.adapters.input.dto.request.PixInput;
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
import java.time.temporal.ChronoUnit;
import java.util.Optional;
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

    private PaymentInput paymentInput;
    private Payment payment;

    @BeforeEach
    void setup() {

        CardInput cardInput = new CardInput(
                "Wei Wuxian",
                "4111111111111111",
                "12/30",
                "123",
                CardType.CREDIT
        );

        paymentInput = new PaymentInput(
                new BigDecimal("100.00"),
                UUID.randomUUID(),
                "Pagamento teste",
                UUID.randomUUID(),
                UUID.randomUUID(),
                PaymentMethod.CARD,
                cardInput,
                null,
                "AAAA-1234"
        );

        payment = Payment.create(
                paymentInput.amount(),
                paymentInput.customerId(),
                paymentInput.description(),
                paymentInput.paymentMethod(),
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

        lenient().when(asyncProcessor.processAsynchronousPayment(any(), any(), any()))
                .thenReturn(PaymentStatus.ACCEPTED);
    }

    @Test
    void deveProcessarPagamentoComCartaoAprovado() {

        when(paymentGateway.process(any(Payment.class))).thenReturn(PaymentStatus.ACCEPTED);

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        when(paymentRepository.findById(any())).thenReturn(Optional.of(payment));

        PaymentResponse response = service.execute(paymentInput);

        assertNotNull(response);
        assertEquals(PaymentStatus.PROCESSING, response.status());

        verify(asyncProcessor, times(1)).processAsynchronousPayment(any(UUID.class), any(UUID.class), any(UUID.class));
    }

    @Test
    void deveIniciarProcessamentoDePagamento() {

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        PaymentResponse response = service.execute(paymentInput);

        assertEquals(PaymentStatus.PROCESSING, response.status());

        verify(paymentRepository, times(1)).save(any(Payment.class));

        verify(asyncProcessor, times(1))
                .processAsynchronousPayment(any(UUID.class), any(UUID.class), any(UUID.class));
    }

    @Test
    void devePublicarEventoAposProcessarPagamento() {

        when(paymentRepository.save(any()))
                .thenReturn(payment);

        when(paymentGateway.process(any()))
                .thenReturn(PaymentStatus.ACCEPTED);

        service.execute(paymentInput);
    }

    @Test
    void deveSalvarPagamentoAntesEDepoisDoProcessamento() {

        when(paymentRepository.save(any()))
                .thenReturn(payment);

        when(paymentGateway.process(any()))
                .thenReturn(PaymentStatus.ACCEPTED);

        service.execute(paymentInput);

        verify(paymentRepository, times(1))
                .save(any(Payment.class));
    }

    @Test
    void deveRetornarPaymentResponse() {

        when(paymentRepository.save(any()))
                .thenReturn(payment);

        when(paymentGateway.process(any()))
                .thenReturn(PaymentStatus.ACCEPTED);

        PaymentResponse response = service.execute(paymentInput);

        assertNotNull(response);
        assertEquals(payment.getCustomerId(), response.customerId());
        assertEquals(payment.getAmount(), response.amount());
    }

    @Test
    void deveCriarPagamentoSemCartaoQuandoPixForInformado() {

        PixInput pix = new PixInput(
                "11999999999",
                Instant.now().plus(30, ChronoUnit.MINUTES)
        );

        PaymentInput request = new PaymentInput(
                new BigDecimal("100.00"),
                UUID.randomUUID(),
                "Pagamento teste",
                UUID.randomUUID(),
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

    @Test
    void deveLancarExcecaoQuandoAmbosCartaoEPixForemInformados() {
        PaymentInput input = new PaymentInput(
                new BigDecimal("100.00"),
                UUID.randomUUID(),
                "Pagamento teste",
                UUID.randomUUID(),
                UUID.randomUUID(),
                PaymentMethod.PIX,
                new CardInput("a", "1234", "12/26", "123", CardType.CREDIT),
                new PixInput("email@email.com", Instant.now()),
                "AAAA-1234"
        );

        assertThrows(PaymentException.class, () -> service.execute(input));
    }

    @Test
    void deveAprovarPagamentoQuandoStatusForSuccess() {
        PaymentInput input = new PaymentInput(
                new BigDecimal("100.00"),
                UUID.randomUUID(),
                "Pagamento teste",
                UUID.randomUUID(),
                UUID.randomUUID(),
                PaymentMethod.PIX,
                null,
                new PixInput("email@email.com", Instant.now()),
                "AAAA-1234"
        );

        Payment paymentMock = mock(Payment.class);
        when(paymentRepository.save(any())).thenReturn(paymentMock);

        when(asyncProcessor.processAsynchronousPayment(any(), any(), any()))
                .thenReturn(PaymentStatus.SUCCESS);

        service.execute(input);

        verify(paymentMock).approve();
        verify(paymentMock, never()).fail();
    }

    @Test
    void deveFalharPagamentoQuandoStatusForRejected() {
        PaymentInput input = new PaymentInput(
                new BigDecimal("100.00"),
                UUID.randomUUID(),
                "Teste",
                UUID.randomUUID(),
                UUID.randomUUID(),
                PaymentMethod.CARD,
                new CardInput("Nome", "123", "12/26", "123", CardType.CREDIT),
                null,
                "CODD-1233"
        );
        Payment paymentMock = mock(Payment.class);
        when(paymentRepository.save(any())).thenReturn(paymentMock);

        when(asyncProcessor.processAsynchronousPayment(any(), any(), any()))
                .thenReturn(PaymentStatus.REJECTED);

        service.execute(input);

        verify(paymentMock).fail();
        verify(paymentMock, never()).approve();
    }

}
