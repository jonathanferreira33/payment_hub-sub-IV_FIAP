package com.fiap.payment_hub.infrastructure.gateway;

import com.fiap.payment_hub.application.ports.output.PaymentGateway;
import com.fiap.payment_hub.domain.entities.Payment;
import com.fiap.payment_hub.domain.enums.PaymentStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MLPaymentGatewayAdapter implements PaymentGateway {

    private final RestTemplate restTemplate;
    private static final Logger log = LoggerFactory.getLogger(MLPaymentGatewayAdapter.class);

    public MLPaymentGatewayAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
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
}
