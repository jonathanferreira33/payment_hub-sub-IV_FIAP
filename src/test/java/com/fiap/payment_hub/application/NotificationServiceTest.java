package com.fiap.payment_hub.application;

import com.fiap.payment_hub.application.dto.request.WebhookPagamentoRequest;
import com.fiap.payment_hub.application.ports.output.PaymentGateway;
import com.fiap.payment_hub.application.services.NotificationService;
import com.fiap.payment_hub.domain.enums.PaymentStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private PaymentGateway paymentGateway;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void deveNotificarGatewayComSucesso() {

        UUID idVeiculo = UUID.randomUUID();
        UUID idVenda = UUID.randomUUID();
        PaymentStatus status = PaymentStatus.ACCEPTED;

        notificationService.notifyAsync(idVeiculo, idVenda, status);

        verify(paymentGateway, times(1)).notifyStatus(
                eq(idVeiculo),
                any(WebhookPagamentoRequest.class)
        );
    }
}