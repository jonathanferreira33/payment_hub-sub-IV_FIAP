package com.fiap.payment_hub.infrastructure.adapters.output.messaging;

import com.fiap.payment_hub.application.ports.output.PaymentEventPublisher;
import com.fiap.payment_hub.domain.entities.Payment;
import com.fiap.payment_hub.infrastructure.config.messaging.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQPaymentEventAdapter implements PaymentEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private static final Logger log =
            LoggerFactory.getLogger(RabbitMQPaymentEventAdapter.class);

    public RabbitMQPaymentEventAdapter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publishPaymentCreated(Payment payment) {
        PaymentCreatedEvent event = PaymentCreatedEvent.fromDomain(payment);

        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.PAYMENT_EXCHANGE,
                    RabbitMQConfig.PAYMENT_ROUTING_KEY,
                    event
            );
            System.out.println("Evento enviado com sucesso para o RabbitMQ!");
        } catch (Exception e) {
            System.err.println("Erro ao enviar para o RabbitMQ: " + e.getMessage());
        }

        log.info("Pagamento {} enviado para RabbitMQ", payment.getId());
    }
}
