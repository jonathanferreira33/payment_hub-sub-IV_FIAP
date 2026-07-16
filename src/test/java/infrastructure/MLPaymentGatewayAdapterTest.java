package infrastructure;

import com.fiap.payment_hub.application.dto.request.WebhookPagamentoRequest;
import com.fiap.payment_hub.application.services.AsyncPaymentProcessor;
import com.fiap.payment_hub.application.services.NotificationService;
import com.fiap.payment_hub.domain.entities.Payment;
import com.fiap.payment_hub.domain.enums.CardType;
import com.fiap.payment_hub.domain.enums.PaymentMethod;
import com.fiap.payment_hub.domain.enums.PaymentStatus;
import com.fiap.payment_hub.domain.valueobjects.Card;
import com.fiap.payment_hub.domain.valueobjects.Pix;
import com.fiap.payment_hub.infrastructure.error.PaymentException;
import com.fiap.payment_hub.infrastructure.gateway.MLPaymentGatewayAdapter;
import com.fiap.payment_hub.shared.enums.StatusPagamento;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MLPaymentGatewayAdapterTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private AsyncPaymentProcessor asyncPaymentProcessor;

    @Spy
    @InjectMocks
    private MLPaymentGatewayAdapter mlPaymentGatewayAdapter;

    @Test
    void deveRetornarAcceptedQuandoProcessarComSucesso() {
        RestClient mockClient = mock(RestClient.class);

        MLPaymentGatewayAdapter adapter = new MLPaymentGatewayAdapter(mockClient);

        Random mockRandom = mock(Random.class);
        when(mockRandom.nextBoolean()).thenReturn(true);
        adapter.setRandom(mockRandom);

        PaymentStatus resultadoStatus = adapter.process(
                new Payment(
                    UUID.randomUUID(),
                    BigDecimal.TEN,
                    UUID.randomUUID(),
                    "teste",
                    PaymentMethod.CARD,
                    PaymentStatus.ACCEPTED,
                    new Card("Mukouda Tsuyoshi","1234","12/30","123", CardType.CREDIT),
                        null,
                        Instant.now(),
                        Instant.now(),
    "ABCD-1234"

                ));
        assertEquals(PaymentStatus.ACCEPTED, resultadoStatus);
    }

    @Test
    void deveNotificarStatusComSucesso() {

        WebhookPagamentoRequest webhookPagamentoRequest = new WebhookPagamentoRequest (
                UUID.randomUUID(),
                StatusPagamento.CONFIRMADO

        );

        doNothing().when(mlPaymentGatewayAdapter).executePatch(any(), any());

        mlPaymentGatewayAdapter.notifyStatus(UUID.randomUUID(), webhookPagamentoRequest);

        verify(mlPaymentGatewayAdapter, times(1)).executePatch(any(), any());
    }

    @Test
    void deveLancarPaymentExceptionQuandoWebhookFalhar() {
        WebhookPagamentoRequest webhookPagamentoRequest = new WebhookPagamentoRequest (
                UUID.randomUUID(),
                StatusPagamento.CONFIRMADO

        );

        doThrow(new PaymentException("Erro", null))
                .when(mlPaymentGatewayAdapter).executePatch(any(), any());

        assertThrows(PaymentException.class, () ->
                mlPaymentGatewayAdapter.notifyStatus(UUID.randomUUID(), webhookPagamentoRequest)
        );
    }

}
