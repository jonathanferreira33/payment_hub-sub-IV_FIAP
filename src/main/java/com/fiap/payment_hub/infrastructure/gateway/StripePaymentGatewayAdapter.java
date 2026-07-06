package com.fiap.payment_hub.infrastructure.gateway;

import com.fiap.payment_hub.application.ports.output.PaymentGateway;
import com.fiap.payment_hub.domain.entities.Payment;
import com.fiap.payment_hub.domain.enums.PaymentStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class StripePaymentGatewayAdapter implements PaymentGateway {

    private final RestTemplate restTemplate;

    public StripePaymentGatewayAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public PaymentStatus process(Payment payment) {
        try {
            System.out.println("Enviando pagamento " + payment.getId() + " via HTTP para API externa da Stripe..."); // TODO: log

            return PaymentStatus.PENDING;

        } catch (Exception e) {
            System.err.println("Falha ao comunicar com a Stripe: " + e.getMessage()); // TODO: log
            return PaymentStatus.FAILED;
        }
    }
}
