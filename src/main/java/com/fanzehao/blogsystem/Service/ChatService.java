package com.fanzehao.blogsystem.Service;

import com.fanzehao.blogsystem.model.WebSocketEmitter;
import com.fanzehao.blogsystem.pojo.ChatMessage;
import com.fanzehao.blogsystem.response.Result;



import java.util.List;


public interface ChatService {
    Result<?> sendMessage(List<ChatMessage> message);
    void sendMessageStream(List<ChatMessage> message, WebSocketEmitter emitter);
}
