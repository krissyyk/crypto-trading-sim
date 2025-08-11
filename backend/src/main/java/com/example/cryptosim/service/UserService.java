package com.example.cryptosim.service;

import com.example.cryptosim.dao.HoldingsDAO;
import com.example.cryptosim.dao.TransactionsDAO;
import com.example.cryptosim.dao.UserDAO;
import com.example.cryptosim.model.Holdings;
import com.example.cryptosim.model.Transactions;
import com.example.cryptosim.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    private final UserDAO userDAO;
    private final HoldingsDAO holdingsDAO;
    private final TransactionsDAO transactionsDAO;

    public UserService(UserDAO userDAO, HoldingsDAO holdingsDAO, TransactionsDAO transactionsDAO) {
        this.userDAO = userDAO;
        this.holdingsDAO = holdingsDAO;
        this.transactionsDAO = transactionsDAO;
    }

    public User getUserById(int userId) {
        return userDAO.findById(userId);
    }

    public List<Transactions> getUserTransactions(int userId) {
        return transactionsDAO.findByUserId(userId);
    }

    public Holdings getUserHolding(int userId, String cryptoName) {
        return holdingsDAO.findByUserIdAndCryptoName(userId, cryptoName);
    }

    @Transactional
    public String buyCrypto(int userId, String cryptoName, BigDecimal quantity, BigDecimal pricePerUnit) {
        if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
            return "Quantity must be positive.";
        }

        User user = userDAO.findById(userId);
        if (user == null) return "User not found.";

        BigDecimal totalCost = pricePerUnit.multiply(quantity);
        if (user.getBalance().compareTo(totalCost) < 0) {
            return "Insufficient balance.";
        }


        BigDecimal newBalance = user.getBalance().subtract(totalCost);
        userDAO.updateBalance(userId, newBalance);


        Holdings holding = holdingsDAO.findByUserIdAndCryptoName(userId, cryptoName);
        if (holding == null) {

            holdingsDAO.insert(new Holdings(0, userId, cryptoName, quantity));
        } else {
            BigDecimal updatedQuantity = holding.getQuantity().add(quantity);
            holdingsDAO.updateQuantity(holding.getId(), updatedQuantity);
        }


        Transactions transaction = new Transactions();
        transaction.setUserId(userId);
        transaction.setCryptoName(cryptoName);
        transaction.setTransactionType("BUY");
        transaction.setQuantity(quantity);
        transaction.setPrice(pricePerUnit);
        transaction.setTotalAmount(totalCost);
        transaction.setTimestamp(LocalDateTime.now());
        transactionsDAO.insert(transaction);

        return "Purchase successful.";
    }

    @Transactional
    public String sellCrypto(int userId, String cryptoName, BigDecimal quantity, BigDecimal pricePerUnit) {
        if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
            return "Quantity must be positive.";
        }

        User user = userDAO.findById(userId);
        if (user == null) return "User not found.";

        Holdings holding = holdingsDAO.findByUserIdAndCryptoName(userId, cryptoName);
        if (holding == null || holding.getQuantity().compareTo(quantity) < 0) {
            return "Insufficient crypto holdings.";
        }

        BigDecimal totalGain = pricePerUnit.multiply(quantity);


        BigDecimal updatedQuantity = holding.getQuantity().subtract(quantity);
        holdingsDAO.updateQuantity(holding.getId(), updatedQuantity);


        BigDecimal newBalance = user.getBalance().add(totalGain);
        userDAO.updateBalance(userId, newBalance);


        Transactions transaction = new Transactions();
        transaction.setUserId(userId);
        transaction.setCryptoName(cryptoName);
        transaction.setTransactionType("SELL");
        transaction.setQuantity(quantity);
        transaction.setPrice(pricePerUnit);
        transaction.setTotalAmount(totalGain);
        transaction.setTimestamp(LocalDateTime.now());
        transactionsDAO.insert(transaction);

        return "Sale successful.";
    }

    @Transactional
    public void resetAccount(int userId, BigDecimal startingBalance) {

        userDAO.updateBalance(userId, startingBalance);


        holdingsDAO.deleteAllByUserId(userId);


    }
}
