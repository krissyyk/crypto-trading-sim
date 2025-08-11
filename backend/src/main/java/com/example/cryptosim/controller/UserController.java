package com.example.cryptosim.controller;

import com.example.cryptosim.model.Holdings;
import com.example.cryptosim.model.Transactions;
import com.example.cryptosim.model.User;
import com.example.cryptosim.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable int userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }


    @GetMapping("/{userId}/holdings/{cryptoName}")
    public ResponseEntity<Holdings> getUserHolding(@PathVariable int userId, @PathVariable String cryptoName) {
        Holdings holding = userService.getUserHolding(userId, cryptoName);
        if (holding == null) {
            Holdings emptyHolding = new Holdings();
            emptyHolding.setUserId(userId);
            emptyHolding.setCryptoName(cryptoName);
            emptyHolding.setQuantity(BigDecimal.ZERO);
            return ResponseEntity.ok(emptyHolding);
        }
        return ResponseEntity.ok(holding);
    }



    @GetMapping("/{userId}/transactions")
    public ResponseEntity<List<Transactions>> getUserTransactions(@PathVariable int userId) {
        List<Transactions> transactions = userService.getUserTransactions(userId);
        return ResponseEntity.ok(transactions);
    }


    public static class CryptoTradeRequest {
        public String cryptoName;
        public BigDecimal quantity;
        public BigDecimal pricePerUnit;
    }


    @PostMapping("/{userId}/buy")
    public ResponseEntity<String> buyCrypto(@PathVariable int userId, @RequestBody CryptoTradeRequest request) {
        String result = userService.buyCrypto(userId, request.cryptoName, request.quantity, request.pricePerUnit);
        if (result.equals("Purchase successful.")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }


    @PostMapping("/{userId}/sell")
    public ResponseEntity<String> sellCrypto(@PathVariable int userId, @RequestBody CryptoTradeRequest request) {
        String result = userService.sellCrypto(userId, request.cryptoName, request.quantity, request.pricePerUnit);
        if (result.equals("Sale successful.")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }


    @PostMapping("/{userId}/reset")
    public ResponseEntity<String> resetAccount(@PathVariable int userId, @RequestParam BigDecimal startingBalance) {
        userService.resetAccount(userId, startingBalance);
        return ResponseEntity.ok("Account reset successful.");
    }
}
