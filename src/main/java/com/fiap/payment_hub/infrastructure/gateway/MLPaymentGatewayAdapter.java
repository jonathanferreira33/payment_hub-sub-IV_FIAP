package com.fiap.payment_hub.infrastructure.gateway;

import com.fiap.payment_hub.application.dto.request.WebhookPagamentoRequest;
import com.fiap.payment_hub.application.ports.output.PaymentGateway;
import com.fiap.payment_hub.domain.entities.Payment;
import com.fiap.payment_hub.domain.enums.PaymentStatus;
import com.fiap.payment_hub.infrastructure.error.PaymentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class MLPaymentGatewayAdapter implements PaymentGateway {

    private final RestClient restClient;

    private static final Logger log = LoggerFactory.getLogger(MLPaymentGatewayAdapter.class);

    public MLPaymentGatewayAdapter(
            @Qualifier("vehicleRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public PaymentStatus process(Payment payment) {
        if ("ERRO".equals(payment.getDescription())) {
            return PaymentStatus.REJECTED;
        }
        return PaymentStatus.ACCEPTED;
    }

    @Override
    public void notifyStatus(WebhookPagamentoRequest request) {
        executePost(request, request.codigoPagamento());
    }

    public void executePost(WebhookPagamentoRequest payload, String codPayment) {
        try {
            restClient.post()
                    .uri("/veiculos/webhook/pagamento")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(payload)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException ex) {
            log.error("Falha ao notificar webhook. Payload: {}", payload, ex);
            throw new PaymentException("Erro ao notificar webhook do veículo: " + codPayment, ex);
        }
    }
}
