package com.example.cryptosim.controller;

import com.example.cryptosim.service.KrakenWebSocketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/prices")
public class PriceController {

    private final KrakenWebSocketService krakenWebSocketService;

    public PriceController(KrakenWebSocketService krakenWebSocketService) {
        this.krakenWebSocketService = krakenWebSocketService;
    }


    @GetMapping("/{symbol}/{currency}")
    public ResponseEntity<BigDecimal> getPrice(
            @PathVariable String symbol,
            @PathVariable String currency) {

        BigDecimal price = krakenWebSocketService.getPrice(symbol.toUpperCase(), currency.toUpperCase());
        if (price == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(price);
    }


    @GetMapping
    public ResponseEntity<Map<String, Map<String, BigDecimal>>> getAllPrices() {
        return ResponseEntity.ok(krakenWebSocketService.getAllPrices());
    }
}
