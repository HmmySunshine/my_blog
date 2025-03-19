package com.fanzehao.blogsystem.Controller;


import com.fanzehao.blogsystem.Service.UserService;
import com.fanzehao.blogsystem.Service.VerificationService;
import com.fanzehao.blogsystem.pojo.EmailDao;
import com.fanzehao.blogsystem.pojo.UserDao;
import com.fanzehao.blogsystem.response.Result;
import com.fanzehao.blogsystem.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/api")
public class RegisterController {
    @Autowired
    private VerificationService verificationService;

    @Autowired
    private UserService userService;

    @PostMapping("/sendverificationcode")
    @ResponseBody
    public Result<?> sendVerificationCode(@RequestBody EmailDao emailDao) {


        return verificationService.sendVerificationCode(emailDao.getEmail());
    }

    @PostMapping("/register")
    @ResponseBody
    public Result<?> register(@RequestBody UserDao userDao) {
        if (!Utils.isUsernameValid(userDao.getUsername())) {
            System.out.println(userDao.getUsername());
            return Result.fail(400,"用户名格式不合法,用户名只能包含字母、数字和下划线，长度在3到16之间");
        }
        if (!Utils.isPasswordValid(userDao.getPassword())) {
            return Result.fail(400,"密码只能包含字母、数字和下划线和.，长度在6到16之间");
        }
        if (!verificationService.VerifyCode(userDao.getEmail(), userDao.getVerificationCode())) {
            return Result.fail("验证码错误");
        }
        return userService.register(userDao.getUsername(), userDao.getPassword(), userDao.getEmail());
    }

}
