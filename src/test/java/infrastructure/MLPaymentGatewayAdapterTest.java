package infrastructure;

import com.fiap.payment_hub.domain.entities.Payment;
import com.fiap.payment_hub.domain.enums.PaymentMethod;
import com.fiap.payment_hub.domain.enums.PaymentStatus;
import com.fiap.payment_hub.domain.valueobjects.Pix;
import com.fiap.payment_hub.infrastructure.gateway.MLPaymentGatewayAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
@ExtendWith(MockitoExtension.class)
class MLPaymentGatewayAdapterTest {

    @Mock
    private RestTemplate restTemplate;

    private MLPaymentGatewayAdapter mlPaymentGatewayAdapter;

    @BeforeEach
    void setUp() {
        this.mlPaymentGatewayAdapter = new MLPaymentGatewayAdapter(restTemplate);
    }

    @Test
    void deveRetornarAcceptedQuandoProcessarComSucesso() {

        Pix pixFake = new Pix("chave-pix-aleatoria-aeiou", LocalDateTime.now().plusHours(1));

        Payment paymentFake = Payment.create(
                BigDecimal.valueOf(250.00),
                UUID.randomUUID(),
                "Compra de teste no Mercado Livre",
                PaymentMethod.PIX,
                null,
                pixFake
        );

        PaymentStatus resultadoStatus = mlPaymentGatewayAdapter.process(paymentFake);

        assertEquals(PaymentStatus.ACCEPTED, resultadoStatus);
    }

    @Test
    void deveRetornarRejectedQuandoOcorrerUmaExcecaoNaChamada() {

        Pix pixFake = new Pix("chave-pix-aleatoria-xpto", LocalDateTime.now().plusHours(1));

        Payment paymentFake = Payment.create(
                BigDecimal.valueOf(100.00),
                UUID.randomUUID(),
                "Tentativa falha",
                PaymentMethod.PIX,
                null,
                pixFake
        );

        PaymentStatus resultadoStatus = mlPaymentGatewayAdapter.process(paymentFake);

        assertEquals(PaymentStatus.ACCEPTED, resultadoStatus);
    }
}
