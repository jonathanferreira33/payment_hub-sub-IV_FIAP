package com.fiap.payment_hub.infrastructure.config.messaging;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String PAYMENT_EXCHANGE = "payment.exchange";
    public static final String PAYMENT_CREATED_QUEUE = "payment.created.queue";
    public static final String PAYMENT_CREATED_ROUTING_KEY = "payment.created.routing-key";

    @Bean
    public TopicExchange paymentExchange() {
        return new TopicExchange(PAYMENT_EXCHANGE);
    }

    @Bean
    public Queue paymentCreatedQueue() {
        return QueueBuilder.durable(PAYMENT_CREATED_QUEUE).build();
    }

    @Bean
    public Binding bindingPaymentCreated(Queue paymentCreatedQueue, TopicExchange paymentExchange) {
        return BindingBuilder
                .bind(paymentCreatedQueue)
                .to(paymentExchange)
                .with(PAYMENT_CREATED_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }
}
