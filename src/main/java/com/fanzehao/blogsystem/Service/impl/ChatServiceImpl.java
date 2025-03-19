package com.fanzehao.blogsystem.Service.impl;

import com.fanzehao.blogsystem.Service.ChatService;
import com.fanzehao.blogsystem.model.WebSocketEmitter;
import com.fanzehao.blogsystem.pojo.ChatMessage;
import com.fanzehao.blogsystem.response.Result;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class ChatServiceImpl implements ChatService {
    @Value("${apiKey}")
    private String apiKey;

    @Value("${model}")
    private String model;

    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(ChatServiceImpl.class);

    final static String url = "https://open.bigmodel.cn/api/paas/v4/chat/completions";

    public ChatServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    //后续记得添加流式传输功能


    @Override
    public void sendMessageStream(List<ChatMessage> messages, WebSocketEmitter emitter) {
        try {
            ResponseEntity<String> response = sendModel(messages);

            if (response.getStatusCode() == HttpStatus.OK && response.hasBody()) {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Objects.requireNonNull(response.getBody()).getBytes());

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(byteArrayInputStream))) {
                    String line;
                    StringBuilder messageBuilder = new StringBuilder();
                    int count = 0;
                    ObjectMapper objectMapper = new ObjectMapper();
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("data: ")) {
                            String jsonData = line.substring(6); // Remove "data: " prefix
                            if ("[DONE]".equals(jsonData)) {
                                emitter.complete();
                                break;
                            }

                            try {
                                JsonNode jsonNode = objectMapper.readTree(jsonData);
                                JsonNode choices = jsonNode.path("choices");

                                for (JsonNode choice : choices) {
                                    String content = choice.path("delta").path("content").asText();
                                    if (!content.isEmpty()) {
                                        count++;
                                        System.out.println(count + " " + content);
                                        emitter.send(new ChatMessage("assistant", content));
                                    }
                                }
                            } catch (Exception e) {
                                logger.error("Error parsing JSON: " + jsonData, e);
                            }
                        }
                    }
                }
            } else {
                logger.error("Error response from API: " + response.getStatusCode());
                emitter.send(new ChatMessage("assistant", "抱歉，处理您的请求时出现了错误。"));
            }
        } catch (Exception e) {
            logger.error("处理消息流时出错", e);
            try {
                emitter.send(new ChatMessage("assistant", "抱歉，处理您的请求时出现了错误。"));
            } catch (Exception ex) {
                logger.error("发送错误消息失败", ex);
            }
        } finally {
            try {
                emitter.complete();
            } catch (Exception e) {
                logger.error("关闭WebSocket连接时出错", e);
            }
        }
    }



    //发送https请求给大模型接口
    public ResponseEntity<String> sendModel(List<ChatMessage> message) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(apiKey);
        // 构造请求体
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);
        requestBody.put("messages", message);
        requestBody.put("stream", true);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, httpHeaders);
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
        System.out.println(response.getBody());
        return response;
    }


    @Override
    public Result<?> sendMessage(List<ChatMessage> message) {
        // TODO Auto-generated method stub
        // 调用大模型接口
        //发送http请求给大模型
        ResponseEntity<String> response = sendModel(message);
        // 解析返回结果
        String StringBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        String content = "";
        try {
            if (StringBody == null)
            {
                logger.info("调用大模型接口返回结果为空");
                return Result.fail();
            }
            JsonNode jsonNode = objectMapper.readTree(StringBody);
            content = jsonNode.path("choices").get(0).path("message").path("content").asText();
            System.out.println("cotent= " + content);
        }
        catch (Exception e) {
            logger.error("解析返回结果失败", e);
        }
        ChatMessage chatMessage = new ChatMessage("assistant", content);

        return Result.success(chatMessage);
    }



}
