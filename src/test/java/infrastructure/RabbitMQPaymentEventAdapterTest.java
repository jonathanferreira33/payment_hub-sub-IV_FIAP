package infrastructure;

import com.fiap.payment_hub.domain.entities.Payment;
import com.fiap.payment_hub.domain.enums.CardType;
import com.fiap.payment_hub.domain.enums.PaymentMethod;
import com.fiap.payment_hub.domain.enums.PaymentStatus;
import com.fiap.payment_hub.domain.valueobjects.Card;
import com.fiap.payment_hub.infrastructure.adapters.output.messaging.PaymentCreatedEvent;
import com.fiap.payment_hub.infrastructure.adapters.output.messaging.RabbitMQPaymentEventAdapter;
import com.fiap.payment_hub.infrastructure.config.messaging.RabbitMQConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RabbitMQPaymentEventAdapterTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private RabbitMQPaymentEventAdapter adapter;

    private Payment payment;

    @BeforeEach
    void setup() {

        payment = Payment.reconstitute(
                UUID.randomUUID(),
                new BigDecimal("100.00"),
                "cliente-123",
                "Pagamento teste",
                PaymentMethod.CARD,
                PaymentStatus.SUCCESS,
                new Card(
                        "Nezuko Kamado",
                        "4111111111111111",
                        "12/30",
                        "123",
                        CardType.CREDIT
                ),
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    void devePublicarEventoDePagamento() {

        adapter.publishPaymentCreated(payment);

        verify(rabbitTemplate).convertAndSend(
                eq(RabbitMQConfig.PAYMENT_EXCHANGE),
                eq(RabbitMQConfig.PAYMENT_ROUTING_KEY),
                any(PaymentCreatedEvent.class)
        );
    }

    @Test
    void devePublicarEventoComDadosCorretos() {

        ArgumentCaptor<PaymentCreatedEvent> captor =
                ArgumentCaptor.forClass(PaymentCreatedEvent.class);

        adapter.publishPaymentCreated(payment);

        verify(rabbitTemplate).convertAndSend(
                eq(RabbitMQConfig.PAYMENT_EXCHANGE),
                eq(RabbitMQConfig.PAYMENT_ROUTING_KEY),
                captor.capture()
        );

        PaymentCreatedEvent event = captor.getValue();

        assertEquals(payment.getId(), event.paymentId());
        assertEquals(payment.getAmount(), event.amount());
        assertEquals(payment.getCustomerId(), event.customerId());
        assertEquals(payment.getPaymentMethod().name(), event.paymentMethod());
        assertEquals(payment.getCreatedAt(), event.createdAt());
    }
}
