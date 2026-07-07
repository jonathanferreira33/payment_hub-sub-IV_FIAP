package infrastructure;

import com.fiap.payment_hub.application.dto.request.PaymentRequest;
import com.fiap.payment_hub.application.ports.input.CreatePaymentUseCase;
import com.fiap.payment_hub.domain.enums.PaymentMethod;
import com.fiap.payment_hub.infrastructure.adapters.input.PaymentQueueListener;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class PaymentQueueListenerTest {

    @Mock
    private CreatePaymentUseCase createPaymentUseCase;

    @InjectMocks
    private PaymentQueueListener paymentQueueListener;

    @Test
    void deveProcessarRequisicaoComSucessoAoReceberMensagem() {
        PaymentRequest requestFake = new PaymentRequest(
                BigDecimal.valueOf(100.00),
                "cust_12345",
                "Assinatura Premium",
                PaymentMethod.PIX,
                null,
                null
        );

        paymentQueueListener.receivePaymentRequest(requestFake);

        verify(createPaymentUseCase, times(1)).execute(requestFake);
    }

    @Test
    void deveCapturarETratarExcecaoQuandoOUseCaseFalhar() {
        PaymentRequest requestFake = new PaymentRequest(
                BigDecimal.valueOf(50.00),
                "cust_99999",
                "Compra Falha",
                PaymentMethod.CARD,
                null,
                null
        );

        doThrow(new IllegalArgumentException("Dados inválidos capturados no validador"))
                .when(createPaymentUseCase).execute(requestFake);

        paymentQueueListener.receivePaymentRequest(requestFake);

        verify(createPaymentUseCase, times(1)).execute(requestFake);
    }
}
