package com.fanzehao.blogsystem.Interceptor;

import com.fanzehao.blogsystem.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Date;
import java.util.Map;

@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    @Autowired
    private JwtUtil jwtUtil;
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        System.out.println(request.getURI());
        //websocket不能发送头请求在url中处理数据
        String token = getTokenFromRequest(request);
        System.out.println(token);
        if (token != null) {
            try {
                Claims claims = jwtUtil.parseToken(token);
                if (claims.getExpiration().before(new Date())) {
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    response.getHeaders().add("Content-Type", "application/json");
                    response.getBody().write("Token has expired".getBytes());
                    return false;
                }
                attributes.put("userId", claims.get("id"));
                return true;
            } catch (Exception e) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                response.getHeaders().add("Content-Type", "application/json");
                response.getBody().write("Invalid token".getBytes());
                return false;
            }
        }
        response.setStatusCode(HttpStatus.FORBIDDEN);
        response.getHeaders().add("Content-Type", "application/json");
        response.getBody().write("Token is required".getBytes());
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }

    private String getTokenFromRequest(ServerHttpRequest request) {
        String query = request.getURI().getQuery();
        if (query != null) {
            String[] params = query.split("&");
            for (String param : params) {
                String[] pair = param.split("=");
                if (pair.length == 2 && "token".equals(pair[0])) {
                    return pair[1];
                }
            }
        }
        return null;
    }
}
