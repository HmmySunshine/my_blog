package com.fanzehao.blogsystem.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    // 可以添加一个构造函数，只初始化 username 和 token
    String username;
    String token;
    String role;
    String userId;
}
