package com.fanzehao.blogsystem.Service.impl;

import com.fanzehao.blogsystem.Service.UserService;

import com.fanzehao.blogsystem.pojo.User;
import com.fanzehao.blogsystem.repository.UserRepository;
import com.fanzehao.blogsystem.response.LoginResponse;
import com.fanzehao.blogsystem.response.Result;
import com.fanzehao.blogsystem.util.JwtUtil;
import com.fanzehao.blogsystem.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;
    //根据用户id和用户名生成token
    public String generateToken(Long id, String username, String role) {
        HashMap<String, Object> objectToken = new HashMap<>();
        objectToken.put("id",id);
        objectToken.put("username",username);
        objectToken.put("role",role);
        return jwtUtil.generateToken(objectToken);
    }
    @Override
    public Result<?> login(String username, String password)
    {
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByUsername(username));
        if (!optionalUser.isPresent())
            return Result.fail(401,"不存在用户名");
        if (!Utils.checkPassword(password,optionalUser.get().getPassword()))
            return Result.fail(401,"密码错误");

        userRepository.updateLastLogin(username);
        if (userRepository.updateLastLogin(username) == 0)
            return Result.fail("更新登录时间失败");
        String token = generateToken(optionalUser.get().getId(),
                optionalUser.get().getUsername(), optionalUser.get().getRole());
        LoginResponse loginResponse = new LoginResponse(optionalUser.get().getUsername(), token,
                optionalUser.get().getRole(), optionalUser.get().getId().toString());
        return Result.success(loginResponse);
    }

    @Override
    public Result<?> register(String username, String password, String email) {
        try {
            if(userRepository.existsByUsername(username))
                return Result.fail(409,"用户名已存在,邮箱已被注册");
            User user = new User();
            //管理员只能这个邮箱测试
            if (email.equals("1647114628@qq.com")) {
                user.setRole("admin");
            }
            user.setUsername(username);
            user.setPassword(Utils.hashPassword(password));
            user.setEmail(email);
            userRepository.save(user);
        }
        catch (Exception e)
        {
            return Result.fail("注册失败");
        }
        return Result.success("注册成功");
    }

}
