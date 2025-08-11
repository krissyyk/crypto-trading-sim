package com.example.cryptosim.config;

import com.example.cryptosim.service.KrakenHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final KrakenHandler handler;

    public WebSocketConfig(KrakenHandler handler) {
        this.handler = handler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(handler, "/ws/prices")
                .setAllowedOrigins("*");
    }
}
