package com.fiap.payment_hub.infrastructure.gateway;

import com.fiap.payment_hub.application.ports.output.PaymentGateway;
import com.fiap.payment_hub.domain.entities.Payment;
import com.fiap.payment_hub.domain.enums.PaymentStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MLPaymentGatewayAdapter implements PaymentGateway {

    private final RestTemplate restTemplate;

    public MLPaymentGatewayAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public PaymentStatus process(Payment payment) {
        try {
            System.out.println("Enviando pagamento " + payment.getId() + " via HTTP para API externa da Mercado Livre..."); // TODO: log

            return PaymentStatus.ACCEPTED;

        } catch (Exception e) {
            System.err.println("Falha ao comunicar com a Mercado Livre: " + e.getMessage()); // TODO: log
            return PaymentStatus.FAILED;
        }
    }
}
