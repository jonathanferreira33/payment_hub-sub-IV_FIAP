package domain;

import com.fiap.payment_hub.domain.entities.Payment;
import com.fiap.payment_hub.domain.enums.CardType;
import com.fiap.payment_hub.domain.enums.PaymentMethod;
import com.fiap.payment_hub.domain.exceptions.InvalidPaymentException;
import com.fiap.payment_hub.domain.validators.PaymentValidator;
import com.fiap.payment_hub.domain.valueobjects.Card;
import com.fiap.payment_hub.domain.valueobjects.Pix;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PaymentValidatorTest {

    @Test
    void deveLancarExcecaoQuandoNumeroCartaoForVazio() {

        Payment payment = Payment.reconstitute(
                null,
                new BigDecimal("10"),
                "cliente",
                "descricao",
                PaymentMethod.CARD,
                null,
                new Card(
                        "Maomao",
                        "",
                        "12/30",
                        "123",
                        CardType.CREDIT
                ),
                null,
                null,
                null
        );

        assertThrows(
                InvalidPaymentException.class,
                () -> PaymentValidator.validate(payment)
        );
    }

    @Test
    void deveLancarExcecaoQuandoCartaoForNulo() {

        Payment payment = Payment.reconstitute(
                null,
                new BigDecimal("10"),
                "cliente",
                "descricao",
                PaymentMethod.CARD,
                null,
                null,
                null,
                null,
                null
        );

        assertThrows(
                InvalidPaymentException.class,
                () -> PaymentValidator.validate(payment)
        );
    }

    @Test
    void deveLancarExcecaoQuandoChavePixForVazia() {

        Payment payment = Payment.reconstitute(
                null,
                new BigDecimal("10"),
                "cliente",
                "descricao",
                PaymentMethod.PIX,
                null,
                null,
                new Pix("", LocalDateTime.now()),
                null,
                null
        );

        assertThrows(
                InvalidPaymentException.class,
                () -> PaymentValidator.validate(payment)
        );
    }

    @Test
    void deveLancarExcecaoQuandoPixForNulo() {

        Payment payment = Payment.reconstitute(
                null,
                new BigDecimal("10"),
                "cliente",
                "descricao",
                PaymentMethod.PIX,
                null,
                null,
                null,
                null,
                null
        );

        assertThrows(
                InvalidPaymentException.class,
                () -> PaymentValidator.validate(payment)
        );
    }

    @Test
    void deveLancarExcecaoQuandoMetodoPagamentoForNulo() {

        Payment payment = Payment.reconstitute(
                null,
                new BigDecimal("10"),
                "cliente",
                "descricao",
                null,
                null,
                null,
                null,
                null,
                null
        );

        assertThrows(
                InvalidPaymentException.class,
                () -> PaymentValidator.validate(payment)
        );
    }

    @Test
    void deveLancarExcecaoQuandoClienteForVazio() {

        Payment payment = Payment.reconstitute(
                null,
                new BigDecimal("10"),
                "",
                "descricao",
                PaymentMethod.CARD,
                null,
                new Card("Mukouda Tsuyoshi","1234","12/30","123", CardType.CREDIT),
                null,
                null,
                null
        );

        assertThrows(
                InvalidPaymentException.class,
                () -> PaymentValidator.validate(payment)
        );
    }

    @Test
    void deveLancarExcecaoQuandoDescricaoForVazia() {

        Payment payment = Payment.reconstitute(
                null,
                new BigDecimal("10"),
                "cliente",
                "",
                PaymentMethod.CARD,
                null,
                new Card("Gabimaru","1234","12/30","123", CardType.CREDIT),
                null,
                null,
                null
        );

        assertThrows(
                InvalidPaymentException.class,
                () -> PaymentValidator.validate(payment)
        );
    }

    @Test
    void deveLancarExcecaoQuandoValorForMenorOuIgualZero() {

        Payment payment = Payment.reconstitute(
                null,
                BigDecimal.ZERO,
                "cliente",
                "descricao",
                PaymentMethod.CARD,
                null,
                new Card("Asta","1234","12/30","123", CardType.CREDIT),
                null,
                null,
                null
        );

        assertThrows(
                InvalidPaymentException.class,
                () -> PaymentValidator.validate(payment)
        );
    }

    @Test
    void deveLancarExcecaoQuandoValorForNulo() {

        Payment payment = Payment.reconstitute(
                null,
                null,
                "cliente",
                "descricao",
                PaymentMethod.CARD,
                null,
                new Card("Yami Sukehiro","1234","12/30","123", CardType.CREDIT),
                null,
                null,
                null
        );

        assertThrows(
                InvalidPaymentException.class,
                () -> PaymentValidator.validate(payment)
        );
    }

    @Test
    void deveLancarExcecaoQuandoPagamentoForNulo() {

        InvalidPaymentException ex = assertThrows(
                InvalidPaymentException.class,
                () -> PaymentValidator.validate(null)
        );

        assertEquals(
                "O pagamento não pode ser nulo.",
                ex.getMessage()
        );
    }

    @Test
    void deveValidarPagamentoValido() {

        Payment payment = criarPagamentoValidoCartao();

        assertDoesNotThrow(() ->
                PaymentValidator.validate(payment)
        );
    }

    private Payment criarPagamentoValidoCartao() {
        return Payment.create(
                new BigDecimal("100.00"),
                "cliente-123",
                "Compra de veículo",
                PaymentMethod.CARD,
                new Card(
                        "Tsukimichi",
                        "4111111111111111",
                        "12/30",
                        "123",
                        CardType.CREDIT
                ),
                null
        );
    }
}
