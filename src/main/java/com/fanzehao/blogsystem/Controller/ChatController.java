package com.fanzehao.blogsystem.Controller;
import com.fanzehao.blogsystem.Interceptor.JwtHandshakeInterceptor;
import com.fanzehao.blogsystem.model.ChatWebSocketHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;




@RestController
@EnableWebSocket
public class ChatController implements WebSocketConfigurer {

    private final ChatWebSocketHandler chatWebSocketHandler;

    @Autowired
    private JwtHandshakeInterceptor jwtHandshakeInterceptor;

    public ChatController(ChatWebSocketHandler chatWebSocketHandler) {
        this.chatWebSocketHandler = chatWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatWebSocketHandler, "/api/chat")
                .addInterceptors(jwtHandshakeInterceptor)
                .setAllowedOrigins("*"); // 在生产环境中，请替换为具体的允许的源
    }
}