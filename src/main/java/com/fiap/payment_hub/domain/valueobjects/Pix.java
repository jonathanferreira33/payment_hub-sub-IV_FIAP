package com.fiap.payment_hub.domain.valueobjects;

import java.time.Instant;

public class Pix {

    private final String key;
    private final Instant expiration;

    public Pix(String key, Instant expiration) {
        this.key = key;
        this.expiration = expiration;
    }

    public String getKey() { return key; }
    public Instant getExpiration() { return expiration; }
}
