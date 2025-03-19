package com.fanzehao.blogsystem.Interceptor;

import com.fanzehao.blogsystem.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class JwtAuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals("OPTIONS")) {
            // 处理预检请求
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }
        System.out.println(request.getRequestURI());
        String token = request.getHeader("Authorization");
        System.out.println(token);
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            try {
                Claims claims = jwtUtil.parseToken(token);
                if (claims.getExpiration().before(new Date())) {
                    // 如果 Token 过期
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 返回401
                    response.getWriter().write("Token has expired");
                    return false;
                }
                // 如果需要，可以将用户信息存储到请求上下文中
                // request.setAttribute("userId", claims.get("id"));
                return true;

            }
            catch (Exception e) {
                // 如果 Token 无效
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid token,权限不足");
                return false;
            }
        }
        // 如果未提供 Token

        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 返回403
        response.getWriter().write("Token is required");
        return false;
    }
}
