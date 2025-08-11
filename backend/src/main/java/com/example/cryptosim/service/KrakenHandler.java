package com.example.cryptosim.service;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;


import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;

@Component
public class KrakenHandler extends TextWebSocketHandler {

    private final KrakenWebSocketService krakenService;
    private final ObjectMapper mapper = new ObjectMapper();

    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

    private ScheduledExecutorService scheduler;

    public KrakenHandler(KrakenWebSocketService krakenService) {
        this.krakenService = krakenService;
    }

    @PostConstruct
    public void startBroadcast() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::broadcastPrices, 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        sendPrices(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }

    private void broadcastPrices() {
        Map<String, Map<String, BigDecimal>> allPrices = krakenService.getAllPrices();
        String message;
        try {
            message = mapper.writeValueAsString(allPrices);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendPrices(WebSocketSession session) throws Exception {
        Map<String, Map<String, BigDecimal>> allPrices = krakenService.getAllPrices();
        String message = mapper.writeValueAsString(allPrices);
        session.sendMessage(new TextMessage(message));
    }
}

