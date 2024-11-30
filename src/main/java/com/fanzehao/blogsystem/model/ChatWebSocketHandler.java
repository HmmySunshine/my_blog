package com.fanzehao.blogsystem.model;

import com.fanzehao.blogsystem.Service.ChatService;
import com.fanzehao.blogsystem.pojo.ChatMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private ChatService chatService;
    private static final Logger logger = LoggerFactory.getLogger(ChatWebSocketHandler.class);
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        System.out.println(payload);
        Map<String, Object> messageMap = objectMapper.readValue(payload, Map.class);
        List<ChatMessage> messages = (List<ChatMessage>) messageMap.get("messages");

        // 异步处理消息
        CompletableFuture.runAsync(() -> {
            try {
                chatService.sendMessageStream(messages, new WebSocketEmitter(session));
            } catch (Exception e) {
                logger.error("处理消息失败", e);
            }
        });
    }
}