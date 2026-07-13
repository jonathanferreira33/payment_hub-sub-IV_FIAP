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
        try {
            log.info("Enviando pagamento {} via HTTP para API externa do Mercado Livre...", payment.getId());
            return PaymentStatus.ACCEPTED;

        } catch (Exception e) {
            log.error("Falha ao comunicar com o Mercado Livre para o pagamento {}: {}", payment.getId(), e.getMessage(), e);
            return PaymentStatus.REJECTED;

        }
    }

    @Override
    public void notifyStatus(String codPayment, String status) {
        var payload = Map.of(
                "codigoPagamento", codPayment,
                "statusPagamento", status
        );

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
