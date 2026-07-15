package infrastructure;

import com.fiap.payment_hub.domain.entities.Payment;
import com.fiap.payment_hub.domain.enums.PaymentMethod;
import com.fiap.payment_hub.domain.enums.PaymentStatus;
import com.fiap.payment_hub.domain.valueobjects.Pix;
import com.fiap.payment_hub.infrastructure.error.PaymentException;
import com.fiap.payment_hub.infrastructure.gateway.MLPaymentGatewayAdapter;
import org.hibernate.mapping.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MLPaymentGatewayAdapterTest {

    @Spy
    @InjectMocks
    private MLPaymentGatewayAdapter mlPaymentGatewayAdapter;

    @Test
    void deveRetornarAcceptedQuandoProcessarComSucesso() {

        Pix pixFake = new Pix("chave-pix-aleatoria-aeiou", Instant.now().plus(60, ChronoUnit.MINUTES));

        Payment paymentFake = Payment.create(
                BigDecimal.valueOf(250.00),
                UUID.randomUUID(),
                "Compra de teste no Mercado Livre",
                PaymentMethod.PIX,
                null,
                pixFake,
                null
        );

        PaymentStatus resultadoStatus = mlPaymentGatewayAdapter.process(paymentFake);

        assertEquals(PaymentStatus.ACCEPTED, resultadoStatus);
    }

    @Test
    void deveNotificarStatusComSucesso() {

        doNothing().when(mlPaymentGatewayAdapter).executePost(any(), anyString());

        mlPaymentGatewayAdapter.notifyStatus("ABCD-1234", "APROVADO");

        verify(mlPaymentGatewayAdapter).executePost(any(), eq("ABCD-1234"));
    }

    @Test
    void deveLancarPaymentExceptionQuandoWebhookFalhar() {
        doThrow(new PaymentException("Erro", null))
                .when(mlPaymentGatewayAdapter).executePost(any(), anyString());

        assertThrows(PaymentException.class, () ->
                mlPaymentGatewayAdapter.notifyStatus("ABCD-1234", "APROVADO")
        );
    }

}
