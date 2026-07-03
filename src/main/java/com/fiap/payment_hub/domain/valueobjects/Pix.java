package com.fiap.payment_hub.domain.valueobjects;

import java.time.LocalDateTime;

public class Pix {

    private final String key;
    private final LocalDateTime expiration;

    public Pix(String key, LocalDateTime expiration) {
        this.key = key;
        this.expiration = expiration;
    }

    public String getKey() { return key; }
    public LocalDateTime getExpiration() { return expiration; }
}
