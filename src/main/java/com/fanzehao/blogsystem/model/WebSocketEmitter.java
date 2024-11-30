package com.fanzehao.blogsystem.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

public class WebSocketEmitter {

    private final WebSocketSession session;
    private final ObjectMapper objectMapper;

    public WebSocketEmitter(WebSocketSession session) {
        this.session = session;
        this.objectMapper = new ObjectMapper();
    }

    public void send(Object data) throws IOException {
        String jsonData = objectMapper.writeValueAsString(data);
        session.sendMessage(new TextMessage(jsonData));
    }

    public void complete() throws IOException {
        session.sendMessage(new TextMessage("{\"type\":\"DONE\"}"));
    }
}