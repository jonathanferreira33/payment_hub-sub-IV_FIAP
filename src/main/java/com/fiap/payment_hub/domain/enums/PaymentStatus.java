package com.fiap.payment_hub.domain.enums;

import java.util.List;

public enum PaymentStatus {
    ACCEPTED,
    PENDING,
    PROCESSING,
    SUCCESS,
    FAILED,
    REJECTED,
    CANCELLED;

    public boolean canTransitionTo(PaymentStatus nextStatus) {
        return switch (this) {
            case ACCEPTED -> List.of(PROCESSING, CANCELLED).contains(nextStatus);
            case PENDING -> List.of(ACCEPTED, PROCESSING, CANCELLED).contains(nextStatus);
            case PROCESSING -> List.of(SUCCESS, FAILED).contains(nextStatus);
            case SUCCESS, FAILED, REJECTED, CANCELLED -> false;
        };
    }
}
