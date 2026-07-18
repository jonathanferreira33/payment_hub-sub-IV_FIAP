package com.fiap.payment_hub.application.services;

import com.fiap.payment_hub.application.dto.request.WebhookPagamentoRequest;
import com.fiap.payment_hub.application.mappers.PaymentMapper;
import com.fiap.payment_hub.application.ports.output.PaymentGateway;
import com.fiap.payment_hub.domain.enums.PaymentStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class NotificationService {

    @Autowired
    private PaymentGateway paymentGateway;

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    @Async("notificationExecutor")
    public void notifyAsync(UUID idVeiculo, UUID idVenda, PaymentStatus status) {
        try {
            Thread.sleep(10_000);

            paymentGateway.notifyStatus(
                    idVeiculo,
                    new WebhookPagamentoRequest(idVenda, PaymentMapper.toWebhookStatus(status))
            );
            log.info("Notificação enviada com sucesso para o veículo: {}", idVeiculo);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error("Erro ao enviar notificação assíncrona", e);
        }
    }
}