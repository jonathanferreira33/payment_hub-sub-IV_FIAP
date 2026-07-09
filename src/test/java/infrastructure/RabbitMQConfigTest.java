package infrastructure;

import com.fiap.payment_hub.infrastructure.config.messaging.RabbitMQConfig;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class RabbitMQConfigTest {

    private final RabbitMQConfig config = new RabbitMQConfig();

    @Test
    void deveCriarExchange() {
        TopicExchange exchange = config.paymentExchange();

        assertNotNull(exchange);
        assertEquals(RabbitMQConfig.PAYMENT_EXCHANGE, exchange.getName());
    }

    @Test
    void deveCriarFila() {
        Queue queue = config.paymentQueue();

        assertNotNull(queue);
        assertEquals(RabbitMQConfig.PAYMENT_REQUEST_QUEUE, queue.getName());
        assertTrue(queue.isDurable());
    }

    @Test
    void deveCriarBinding() {
        Queue queue = config.paymentQueue();
        TopicExchange exchange = config.paymentExchange();

        Binding binding = config.bindingPayment(queue, exchange);

        assertNotNull(binding);
        assertEquals(queue.getName(), binding.getDestination());
        assertEquals(exchange.getName(), binding.getExchange());
        assertEquals(
                RabbitMQConfig.PAYMENT_ROUTING_KEY,
                binding.getRoutingKey()
        );
    }

    @Test
    void deveCriarMessageConverter() {
        Jackson2JsonMessageConverter converter = config.messageConverter();

        assertNotNull(converter);
    }

    @Test
    void deveCriarRabbitTemplate() {
        ConnectionFactory connectionFactory = mock(ConnectionFactory.class);
        Jackson2JsonMessageConverter converter = config.messageConverter();

        RabbitTemplate template = config.rabbitTemplate(connectionFactory, converter);

        assertNotNull(template);
        assertSame(connectionFactory, template.getConnectionFactory());
    }
}
