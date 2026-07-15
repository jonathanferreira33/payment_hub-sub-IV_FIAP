package com.fiap.payment_hub.infrastructure.gateway;

import com.fiap.payment_hub.application.ports.output.PaymentGateway;
import com.fiap.payment_hub.domain.entities.Payment;
import com.fiap.payment_hub.domain.enums.PaymentStatus;
import com.fiap.payment_hub.infrastructure.error.PaymentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Map;

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
    public void notifyStatus(String codPayment, String status) {
        var payload = Map.of(
                "codigoPagamento", codPayment,
                "statusPagamento", status
        );

        executePost(payload, codPayment);
    }

    public void executePost(Map<String, String> payload, String codPayment) {
        try {
            restClient.post()
                    .uri("/api/veiculos/webhook/pagamento")
                    .body(payload)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException ex) {
            throw new PaymentException("Erro ao notificar webhook do veículo: " + codPayment, ex);
        }
    }
}
