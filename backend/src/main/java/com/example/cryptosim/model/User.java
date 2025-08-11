package com.example.cryptosim.model;
import java.math.BigDecimal;
public class User {

        private int id;
        private String username;
        private BigDecimal balance;

        public User() {}

        public User(int id, String username, BigDecimal balance) {
            this.id = id;
            this.username = username;
            this.balance = balance;
        }

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public BigDecimal getBalance() { return balance; }
        public void setBalance(BigDecimal balance) { this.balance = balance; }
    }


