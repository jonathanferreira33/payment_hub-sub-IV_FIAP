package com.fiap.payment_hub.application.services;

import com.fiap.payment_hub.application.ports.output.PaymentGateway;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncPaymentProcessor {

    private final PaymentGateway paymentGateway;

    public AsyncPaymentProcessor(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    @Async
    public void processAsynchronousPayment(String codigoPagamento) {
        try {

            Thread.sleep(10000);

            String status = (Math.random() > 0.2) ? "APROVADO" : "CANCELADO";

            paymentGateway.notifyStatus(codigoPagamento, status);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
