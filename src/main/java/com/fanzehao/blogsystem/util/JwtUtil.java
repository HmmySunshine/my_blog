package com.fanzehao.blogsystem.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;  
import io.jsonwebtoken.security.Keys;  
import org.springframework.beans.factory.annotation.Value;  
import org.springframework.stereotype.Component;  

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;  
import java.util.Date;
import java.util.Map;  

@Component  
public class JwtUtil {  
    // 从配置文件中获取密钥
    @Value("${jwt.secret}")
    private String secret;

    // 从配置文件中获取过期时间
    @Value("${jwt.expiration}")
    private long expiration;


    // 生成token
    public  String generateToken(Map<String, Object> claims) {
        //如果身份是管理员过期时间为1天
        if(claims.get("role").equals("admin"))
            expiration = 86400000;
        else
            expiration = 3600000;
        // 使用密钥生成SecretKey
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        // 使用Jwts生成token
        return Jwts.builder().setClaims(claims)
                // 设置过期时间
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                // 使用密钥签名
                .signWith(key)
                // 压缩token
                .compact();
    }

    // 解析token
    public Claims parseToken(String token) {
        // 使用密钥生成SecretKey
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        // 使用Jwts解析token
        return Jwts.parserBuilder()
                .setSigningKey(key)
                // 解析token
                .build()
                .parseClaimsJws(token)
                // 获取token中的payload
                .getBody();  
    }  
}