package com.fiap.payment_hub.domain.enums;

import java.util.List;

public enum PaymentStatus {
    PENDING,
    PROCESSING,
    SUCCESS,
    FAILED,
    CANCELLED;

    public boolean canTransitionTo(PaymentStatus nextStatus) {
        return switch (this) {
            case PENDING -> List.of(PROCESSING, CANCELLED).contains(nextStatus);
            case PROCESSING -> List.of(SUCCESS, FAILED).contains(nextStatus);
            case SUCCESS, FAILED, CANCELLED -> false;
        };
    }
}
