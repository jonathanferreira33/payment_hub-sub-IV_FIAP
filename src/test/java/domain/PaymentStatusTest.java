package domain;

import com.fiap.payment_hub.domain.enums.PaymentStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentStatusTest {

    @Test
    void devePermitirTransicoesDoStatusAccepted() {
        assertTrue(PaymentStatus.ACCEPTED.canTransitionTo(PaymentStatus.PROCESSING));
        assertTrue(PaymentStatus.ACCEPTED.canTransitionTo(PaymentStatus.CANCELLED));

        assertFalse(PaymentStatus.ACCEPTED.canTransitionTo(PaymentStatus.ACCEPTED));
        assertFalse(PaymentStatus.ACCEPTED.canTransitionTo(PaymentStatus.PENDING));
        assertFalse(PaymentStatus.ACCEPTED.canTransitionTo(PaymentStatus.SUCCESS));
        assertFalse(PaymentStatus.ACCEPTED.canTransitionTo(PaymentStatus.FAILED));
        assertFalse(PaymentStatus.ACCEPTED.canTransitionTo(PaymentStatus.REJECTED));
    }

    @Test
    void devePermitirTransicoesDoStatusPending() {
        assertTrue(PaymentStatus.PENDING.canTransitionTo(PaymentStatus.ACCEPTED));
        assertTrue(PaymentStatus.PENDING.canTransitionTo(PaymentStatus.PROCESSING));
        assertTrue(PaymentStatus.PENDING.canTransitionTo(PaymentStatus.CANCELLED));

        assertFalse(PaymentStatus.PENDING.canTransitionTo(PaymentStatus.PENDING));
        assertFalse(PaymentStatus.PENDING.canTransitionTo(PaymentStatus.SUCCESS));
        assertFalse(PaymentStatus.PENDING.canTransitionTo(PaymentStatus.FAILED));
        assertFalse(PaymentStatus.PENDING.canTransitionTo(PaymentStatus.REJECTED));
    }

    @Test
    void devePermitirTransicoesDoStatusProcessing() {
        assertTrue(PaymentStatus.PROCESSING.canTransitionTo(PaymentStatus.SUCCESS));
        assertTrue(PaymentStatus.PROCESSING.canTransitionTo(PaymentStatus.FAILED));

        assertFalse(PaymentStatus.PROCESSING.canTransitionTo(PaymentStatus.ACCEPTED));
        assertFalse(PaymentStatus.PROCESSING.canTransitionTo(PaymentStatus.PENDING));
        assertFalse(PaymentStatus.PROCESSING.canTransitionTo(PaymentStatus.PROCESSING));
        assertFalse(PaymentStatus.PROCESSING.canTransitionTo(PaymentStatus.REJECTED));
        assertFalse(PaymentStatus.PROCESSING.canTransitionTo(PaymentStatus.CANCELLED));
    }

    @Test
    void naoDevePermitirTransicoesDoStatusSuccess() {
        for (PaymentStatus status : PaymentStatus.values()) {
            assertFalse(PaymentStatus.SUCCESS.canTransitionTo(status));
        }
    }

    @Test
    void naoDevePermitirTransicoesDoStatusFailed() {
        for (PaymentStatus status : PaymentStatus.values()) {
            assertFalse(PaymentStatus.FAILED.canTransitionTo(status));
        }
    }

    @Test
    void naoDevePermitirTransicoesDoStatusRejected() {
        for (PaymentStatus status : PaymentStatus.values()) {
            assertFalse(PaymentStatus.REJECTED.canTransitionTo(status));
        }
    }

    @Test
    void naoDevePermitirTransicoesDoStatusCancelled() {
        for (PaymentStatus status : PaymentStatus.values()) {
            assertFalse(PaymentStatus.CANCELLED.canTransitionTo(status));
        }
    }
}