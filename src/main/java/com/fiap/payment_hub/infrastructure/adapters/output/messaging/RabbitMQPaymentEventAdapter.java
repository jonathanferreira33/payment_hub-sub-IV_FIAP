package com.fiap.payment_hub.infrastructure.adapters.output.messaging;

import com.fiap.payment_hub.application.ports.output.PaymentEventPublisher;
import com.fiap.payment_hub.domain.entities.Payment;
import com.fiap.payment_hub.infrastructure.config.messaging.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQPaymentEventAdapter implements PaymentEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQPaymentEventAdapter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publishPaymentCreated(Payment payment) {
        PaymentCreatedEvent event = PaymentCreatedEvent.fromDomain(payment);

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PAYMENT_EXCHANGE,
                RabbitMQConfig.PAYMENT_CREATED_ROUTING_KEY,
                event
        );

        System.out.println("Mensagem de pagamento enviado com sucesso para a exchange: " + payment.getId()); //TODO: log
    }
}
