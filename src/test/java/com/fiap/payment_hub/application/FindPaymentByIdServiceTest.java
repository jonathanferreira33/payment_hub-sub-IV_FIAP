package com.fiap.payment_hub.application;

import com.fiap.payment_hub.application.dto.response.PaymentResponse;
import com.fiap.payment_hub.application.mappers.PaymentAppMapper;
import com.fiap.payment_hub.application.ports.output.PaymentRepository;
import com.fiap.payment_hub.application.services.FindPaymentByIdService;
import com.fiap.payment_hub.domain.entities.Payment;
import com.fiap.payment_hub.domain.exceptions.PaymentNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindPaymentByIdServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private FindPaymentByIdService findPaymentByIdService;

    private UUID paymentId;
    private Payment mockPayment;
    private PaymentResponse mockPaymentResponse;

    @BeforeEach
    void setUp() {
        paymentId = UUID.randomUUID();
        mockPayment = mock(Payment.class);
        mockPaymentResponse = mock(PaymentResponse.class);
    }

    @Test
    void execute_DeveRetornarPaymentResponse_QuandoPagamentoExistir() {

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(mockPayment));

        try (MockedStatic<PaymentAppMapper> mapperMock = mockStatic(PaymentAppMapper.class)) {
            mapperMock.when(() -> PaymentAppMapper.domainToResponse(mockPayment))
                    .thenReturn(mockPaymentResponse);

            PaymentResponse result = findPaymentByIdService.execute(paymentId);

            assertNotNull(result);
            assertEquals(mockPaymentResponse, result);

            verify(paymentRepository, times(1)).findById(paymentId);
            mapperMock.verify(() -> PaymentAppMapper.domainToResponse(mockPayment), times(1));
        }
    }

    @Test
    void execute_DeveLancarPaymentNotFoundException_QuandoPagamentoNaoExistir() {
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());

        PaymentNotFoundException exception = assertThrows(
                PaymentNotFoundException.class,
                () -> findPaymentByIdService.execute(paymentId)
        );

        assertEquals("Pagamento não encontrado com ID: " + paymentId, exception.getMessage());

        verify(paymentRepository, times(1)).findById(paymentId);
    }
}