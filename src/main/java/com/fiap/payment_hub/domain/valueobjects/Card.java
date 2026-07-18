package com.fiap.payment_hub.domain.valueobjects;

import com.fiap.payment_hub.domain.enums.CardType;

public class Card {

    private final String holderName;
    private final String number;
    private final String expiration;
    private final String cvv;
    private final CardType type;

    public Card(String holderName, String number, String expiration, String cvv, CardType type) {
        this.holderName = holderName;
        this.number = number;
        this.expiration = expiration;
        this.cvv = cvv;
        this.type = type;
    }

    public String getHolderName() { return holderName; }
    public String getNumber() { return number; }
    public String getExpiration() { return expiration; }
    public String getCvv() { return cvv; }
    public CardType getType() { return type; }
}