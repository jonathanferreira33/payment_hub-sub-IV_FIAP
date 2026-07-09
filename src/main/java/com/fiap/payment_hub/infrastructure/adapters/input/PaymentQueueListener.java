package com.fiap.payment_hub.infrastructure.adapters.input;

import com.fiap.payment_hub.application.dto.request.PaymentRequest;
import com.fiap.payment_hub.application.ports.input.CreatePaymentUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentQueueListener {

    private final CreatePaymentUseCase createPaymentUseCase;
    private static final Logger log = LoggerFactory.getLogger(PaymentQueueListener.class);

    public PaymentQueueListener(CreatePaymentUseCase createPaymentUseCase) {
        this.createPaymentUseCase = createPaymentUseCase;
    }

    @RabbitListener(queues = "payment.request.queue")
    public void receivePaymentRequest(PaymentRequest request) {

        log.info("Mensagem bruta recebida no Listener: {}", request);

        try {
            log.info("Nova requisição de pagamento recebida via RabbitMQ para o cliente: {}",
                    request != null ? request.customerId() : "NULL");

            log.info("Nova requisição de pagamento recebida via RabbitMQ coma descrição: {}",
                    request != null ? request.description() : "NULL");
            createPaymentUseCase.execute(request);

        } catch (Exception e) {
            log.error("Erro ao processar mensagem da fila para o cliente {}: {}",
                    request != null ? request.customerId() : "NULL",
                    e.getMessage(), e);
        }
    }
}
