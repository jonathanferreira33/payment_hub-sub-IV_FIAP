package com.fiap.payment_hub.application.services;

import com.fiap.payment_hub.application.dto.request.WebhookPagamentoRequest;
import com.fiap.payment_hub.application.mappers.PaymentMapper;
import com.fiap.payment_hub.application.ports.output.PaymentGateway;
import com.fiap.payment_hub.application.ports.output.PaymentRepository;
import com.fiap.payment_hub.domain.entities.Payment;
import com.fiap.payment_hub.domain.enums.PaymentStatus;
import com.fiap.payment_hub.infrastructure.error.PaymentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AsyncPaymentProcessor {

    private final PaymentGateway paymentGateway;
    private final PaymentRepository repository;
    private static final Logger log = LoggerFactory.getLogger(AsyncPaymentProcessor.class);

    public AsyncPaymentProcessor(PaymentGateway paymentGateway, PaymentRepository repository) {
        this.paymentGateway = paymentGateway;
        this.repository = repository;
    }

    @Async
    public void processAsynchronousPayment(UUID idPagamento, UUID idVenda) {

        try {
            log.info("Inicio Processamento de pagamento " + idPagamento + " payment-hub");

            Thread.sleep(10_000);

            Payment payment = repository.findById(idPagamento)
                    .orElseThrow(() ->
                            new PaymentException("Pagamento não encontrado para o ID: " +idPagamento));

            PaymentStatus status = paymentGateway.process(payment);

            log.info("Envio de notificação ao Webhook, codigo de pagamento: " + payment.getPaymentCode());

            paymentGateway.notifyStatus(
                    new WebhookPagamentoRequest(
                            idVenda,
                            payment.getAmount(),
                            payment.getPaymentCode(),
                            PaymentMapper.toWebhookStatus(status)
                    )
            );

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
