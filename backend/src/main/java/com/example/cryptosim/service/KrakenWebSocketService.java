package com.example.cryptosim.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.net.URI;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class KrakenWebSocketService {

    private WebSocketClient client;
    private final Map<String, Map<String, BigDecimal>> prices = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final List<String> KRAKEN_PAIRS = List.of(
            "XBT/USD", "XBT/EUR",
            "ETH/USD", "ETH/EUR",
            "ADA/USD", "ADA/EUR",
            "DOT/USD", "DOT/EUR",
            "SOL/USD", "SOL/EUR",
            "LTC/USD", "LTC/EUR",
            "XRP/USD", "XRP/EUR",
            "LINK/USD", "LINK/EUR",
            "BCH/USD", "BCH/EUR",
            "TRX/USD", "TRX/EUR",
            "ETC/USD", "ETC/EUR",
            "XMR/USD", "XMR/EUR",
            "CRV/USD", "CRV/EUR",
            "ATOM/USD", "ATOM/EUR",
            "UNI/USD", "UNI/EUR",
            "AVAX/USD", "AVAX/EUR",
            "AAVE/USD", "AAVE/EUR",
            "FIL/USD", "FIL/EUR",
            "XTZ/USD", "XTZ/EUR",
            "ALGO/USD", "ALGO/EUR"
    );

    @PostConstruct
    public void start() {
        try {
            client = new WebSocketClient(new URI("wss://ws.kraken.com")) {
                @Override
                public void onOpen(ServerHandshake handshake) {
                    System.out.println("Connected to Kraken WebSocket");
                    subscribeToTickers();
                }

                @Override
                public void onMessage(String message) {
                    handleMessage(message);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    System.out.println("Connection closed: " + reason);
                }

                @Override
                public void onError(Exception ex) {
                    System.err.println("WebSocket error:");
                    ex.printStackTrace();
                }
            };
            client.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void subscribeToTickers() {
        Map<String, Object> subscribe = new HashMap<>();
        subscribe.put("event", "subscribe");
        subscribe.put("pair", KRAKEN_PAIRS);
        subscribe.put("subscription", Map.of("name", "ticker"));

        try {
            String subscribeMsg = objectMapper.writeValueAsString(subscribe);
            client.send(subscribeMsg);
            System.out.println("Sent subscription: " + subscribeMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleMessage(String message) {
        try {
            JsonNode root = objectMapper.readTree(message);

            if (root.has("event")) {
                // Ignore heartbeat or subscription status events
                return;
            }

            if (root.isArray() && root.size() > 1) {
                JsonNode tickerData = root.get(1);
                JsonNode pairNode = root.get(3);

                if (tickerData != null && pairNode != null) {
                    String krakenPair = pairNode.asText();
                    String[] parts = krakenPair.split("/");

                    if (parts.length == 2 && tickerData.has("c")) {
                        String cryptoSymbol = parts[0];
                        String currency = parts[1];
                        String priceStr = tickerData.get("c").get(0).asText();
                        BigDecimal price = new BigDecimal(priceStr);

                        prices.computeIfAbsent(cryptoSymbol, k -> new ConcurrentHashMap<>())
                                .put(currency, price);

                        System.out.printf("Updated %s/%s price: %s%n", cryptoSymbol, currency, price);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to parse Kraken message:");
            System.err.println(message);
            e.printStackTrace();
        }
    }

    public BigDecimal getPrice(String cryptoSymbol, String currency) {
        return prices.getOrDefault(cryptoSymbol, Collections.emptyMap()).get(currency);
    }

    public Map<String, Map<String, BigDecimal>> getAllPrices() {
        return Collections.unmodifiableMap(prices);
    }
}
