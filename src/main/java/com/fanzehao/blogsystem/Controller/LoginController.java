package com.fanzehao.blogsystem.Controller;

import com.fanzehao.blogsystem.Service.UserService;
import com.fanzehao.blogsystem.pojo.UserDao;
import com.fanzehao.blogsystem.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api")
public class LoginController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @ResponseBody
    public Result<?> login(@RequestBody UserDao userDao){
       return userService.login(userDao.getUsername(), userDao.getPassword());
    }
}
