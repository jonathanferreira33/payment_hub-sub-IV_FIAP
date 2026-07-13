package infrastructure;

import com.fiap.payment_hub.domain.entities.Payment;
import com.fiap.payment_hub.domain.enums.CardType;
import com.fiap.payment_hub.domain.enums.PaymentMethod;
import com.fiap.payment_hub.domain.enums.PaymentStatus;
import com.fiap.payment_hub.domain.valueobjects.Card;
import com.fiap.payment_hub.domain.valueobjects.Pix;
import com.fiap.payment_hub.infrastructure.adapters.output.entity.JpaPaymentEntity;
import com.fiap.payment_hub.infrastructure.adapters.output.mapper.PaymentInfrastructureMapper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PaymentInfrastructureMapperTest {

    @Test
    void deveConverterDomainParaJpaComCartao() {

        Card card = new Card(
                "Erza Scarlet",
                "4111111111111111",
                "12/30",
                "123",
                CardType.CREDIT
        );

        Payment payment = Payment.reconstitute(
                UUID.randomUUID(),
                new BigDecimal("150.00"),
                UUID.randomUUID(),
                "Compra",
                PaymentMethod.CARD,
                PaymentStatus.SUCCESS,
                card,
                null,
                LocalDateTime.now(),
                LocalDateTime.now(),
                "ABCD-1345"
        );

        JpaPaymentEntity entity =
                PaymentInfrastructureMapper.domainToJpa(payment);

        assertNotNull(entity);
        assertEquals(payment.getId(), entity.getId());
        assertEquals("Erza Scarlet", entity.getCardHolderName());
        assertEquals("4111111111111111", entity.getCardNumber());
        assertEquals("12/30", entity.getCardExpiration());
        assertEquals("CREDIT", entity.getCardType());
    }

    @Test
    void deveConverterDomainParaJpaComPix() {

        Pix pix = new Pix(
                "11999999999",
                LocalDateTime.now().plusMinutes(30)
        );

        Payment payment = Payment.reconstitute(
                UUID.randomUUID(),
                new BigDecimal("50"),
                UUID.randomUUID(),
                "Pix",
                PaymentMethod.PIX,
                PaymentStatus.PENDING,
                null,
                pix,
                LocalDateTime.now(),
                null,
                "ABCD-1345"

        );

        JpaPaymentEntity entity =
                PaymentInfrastructureMapper.domainToJpa(payment);

        assertNotNull(entity);
        assertEquals("11999999999", entity.getPixKey());
        assertEquals(pix.getExpiration(), entity.getPixExpiration());

        assertNull(entity.getCardHolderName());
        assertNull(entity.getCardNumber());
    }

    @Test
    void deveConverterJpaParaDomainComCartao() {

        JpaPaymentEntity entity = new JpaPaymentEntity(
                UUID.randomUUID(),
                new BigDecimal("100"),
                UUID.randomUUID(),
                "Compra",
                "CARD",
                "SUCCESS",
                LocalDateTime.now(),
                LocalDateTime.now(),
                "Boa Hancock",
                "4111111111111111",
                "12/30",
                "CREDIT",
                null,
                null,
                "ABCD-1345"

        );

        Payment payment =
                PaymentInfrastructureMapper.jpaToDomain(entity);

        assertNotNull(payment);
        assertNotNull(payment.getCard());
        assertEquals("Boa Hancock",
                payment.getCard().getHolderName());

        assertEquals("4111111111111111",
                payment.getCard().getNumber());

        assertEquals(CardType.CREDIT,
                payment.getCard().getType());

        assertNull(payment.getPix());
        assertNull(payment.getCard().getCvv());
    }

    @Test
    void deveConverterJpaParaDomainComPix() {

        LocalDateTime expiration =
                LocalDateTime.now().plusMinutes(30);

        JpaPaymentEntity entity = new JpaPaymentEntity(
                UUID.randomUUID(),
                new BigDecimal("30"),
                UUID.randomUUID(),
                "Pix",
                "PIX",
                "PENDING",
                LocalDateTime.now(),
                null,
                null,
                null,
                null,
                null,
                "pix-chave",
                expiration,
                "ABCD-1345"

        );

        Payment payment =
                PaymentInfrastructureMapper.jpaToDomain(entity);

        assertNotNull(payment);
        assertNull(payment.getCard());

        assertEquals("pix-chave",
                payment.getPix().getKey());

        assertEquals(expiration,
                payment.getPix().getExpiration());
    }

    @Test
    void deveRetornarNullQuandoDomainForNull() {

        assertNull(
                PaymentInfrastructureMapper.domainToJpa(null)
        );
    }

    @Test
    void deveRetornarNullQuandoJpaForNull() {

        assertNull(
                PaymentInfrastructureMapper.jpaToDomain(null)
        );
    }

    @Test
    void deveConverterDomainSemCartaoEPix() {

        Payment payment = Payment.reconstitute(
                UUID.randomUUID(),
                new BigDecimal("10"),
                UUID.randomUUID(),
                "Pagamento",
                PaymentMethod.CARD,
                PaymentStatus.PENDING,
                null,
                null,
                LocalDateTime.now(),
                null,
                null
        );

        JpaPaymentEntity entity =
                PaymentInfrastructureMapper.domainToJpa(payment);

        assertNull(entity.getCardHolderName());
        assertNull(entity.getCardNumber());
        assertNull(entity.getPixKey());
    }
}
