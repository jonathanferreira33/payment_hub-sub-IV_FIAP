package infrastructure;

import com.fiap.payment_hub.domain.entities.Payment;
import com.fiap.payment_hub.domain.enums.CardType;
import com.fiap.payment_hub.domain.enums.PaymentMethod;
import com.fiap.payment_hub.domain.enums.PaymentStatus;
import com.fiap.payment_hub.domain.valueobjects.Card;
import com.fiap.payment_hub.infrastructure.adapters.output.entity.JpaPaymentEntity;
import com.fiap.payment_hub.infrastructure.adapters.output.mapper.PaymentInfrastructureMapper;
import com.fiap.payment_hub.infrastructure.adapters.output.repositories.PaymentRepositoryImpl;
import com.fiap.payment_hub.infrastructure.adapters.output.repositories.SpringDataPaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentRepositoryImplTest {

    @Mock
    private SpringDataPaymentRepository repository;

    @InjectMocks
    private PaymentRepositoryImpl paymentRepository;

    @Test
    void deveSalvarPagamento() {

        Payment payment = Payment.reconstitute(
                UUID.randomUUID(),
                BigDecimal.TEN,
                UUID.randomUUID(),
                "Compra",
                PaymentMethod.CARD,
                PaymentStatus.SUCCESS,
                new Card(
                        "Nami",
                        "1234123412341234",
                        "12/30",
                        "123",
                        CardType.CREDIT
                ),
                null,
                Instant.now(),
                Instant.now(),
                null
        );

        JpaPaymentEntity entity = PaymentInfrastructureMapper.domainToJpa(payment);

        when(repository.save(any(JpaPaymentEntity.class)))
                .thenReturn(entity);

        Payment result = paymentRepository.save(payment);

        verify(repository).save(any(JpaPaymentEntity.class));

        assertNotNull(result);
        assertEquals(payment.getId(), result.getId());
        assertEquals(payment.getAmount(), result.getAmount());
        assertEquals(payment.getCustomerId(), result.getCustomerId());
        assertEquals(payment.getStatus(), result.getStatus());
    }

    @Test
    void deveRetornarPagamentoQuandoEncontrado() {
        UUID id = UUID.randomUUID();

        Payment payment = Payment.reconstitute(
                id,
                BigDecimal.valueOf(150),
                UUID.randomUUID(),
                "Compra de veículo",
                PaymentMethod.CARD,
                PaymentStatus.SUCCESS,
                new Card(
                        "Tatsumaki",
                        "1234123412341234",
                        "12/30",
                        "123",
                        CardType.CREDIT
                ),
                null,
                Instant.now(),
                Instant.now(),
                null
        );

        JpaPaymentEntity entity = PaymentInfrastructureMapper.domainToJpa(payment);

        when(repository.findById(id))
                .thenReturn(Optional.of(entity));

        Optional<Payment> resultado = paymentRepository.findById(id);

        assertTrue(resultado.isPresent());

        Payment encontrado = resultado.get();

        assertEquals(payment.getId(), encontrado.getId());
        assertEquals(payment.getAmount(), encontrado.getAmount());
        assertEquals(payment.getCustomerId(), encontrado.getCustomerId());
        assertEquals(payment.getDescription(), encontrado.getDescription());
        assertEquals(payment.getPaymentMethod(), encontrado.getPaymentMethod());
        assertEquals(payment.getStatus(), encontrado.getStatus());

        verify(repository).findById(id);
    }

    @Test
    void deveRetornarOptionalVazioQuandoNaoEncontrado() {
        UUID id = UUID.randomUUID();

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        Optional<Payment> resultado = paymentRepository.findById(id);

        assertTrue(resultado.isEmpty());

        verify(repository).findById(id);
    }
}
