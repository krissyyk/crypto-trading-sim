package com.example.cryptosim.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transactions {
    private int id;
    private int userId;
    private String cryptoName;
    private String transactionType;
    private BigDecimal quantity;
    private BigDecimal price;
    private BigDecimal totalAmount;
    private LocalDateTime timestamp;

    public Transactions() {
    }

    public Transactions(int id, int userId, String cryptoName, String transactionType, BigDecimal quantity, BigDecimal price, BigDecimal totalAmount, LocalDateTime timestamp) {
        this.id = id;
        this.userId = userId;
        this.cryptoName = cryptoName;
        this.transactionType = transactionType;
        this.quantity = quantity;
        this.price = price;
        this.totalAmount = totalAmount;
        this.timestamp = timestamp;
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

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}