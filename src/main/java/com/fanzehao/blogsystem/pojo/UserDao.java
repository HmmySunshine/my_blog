package com.fanzehao.blogsystem.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDao {
    protected String username;
    private String email;
    private String password;
    private String verificationCode;
    protected String token;

}
