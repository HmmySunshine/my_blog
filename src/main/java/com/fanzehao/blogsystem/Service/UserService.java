package com.fanzehao.blogsystem.Service;

import com.fanzehao.blogsystem.response.Result;

public interface UserService {
   Result<?> login(String username, String password);
   Result<?> register(String username, String password, String email);



}
