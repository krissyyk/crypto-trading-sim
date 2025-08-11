package com.example.cryptosim.model;

import java.math.BigDecimal;

public class Holdings {
    private int id;
    private int userId;
    private String cryptoName;
    private BigDecimal quantity;

    public Holdings() {
    }

    public Holdings(int id, int userId, String cryptoName, BigDecimal quantity) {
        this.id = id;
        this.userId = userId;
        this.cryptoName = cryptoName;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCryptoName() {
        return cryptoName;
    }

    public void setCryptoName(String cryptoName) {
        this.cryptoName = cryptoName;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
}

