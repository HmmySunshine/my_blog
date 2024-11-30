package com.fanzehao.blogsystem.Service;

import com.fanzehao.blogsystem.repository.UserRepository;
import com.fanzehao.blogsystem.response.Result;
import com.fanzehao.blogsystem.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class VerificationService {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private UserRepository userRepository;
    private final static Integer TIME_OUT = 5;
    private final static String KEY_PREFIX = "email_verification:";

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public Result<?> sendVerificationCode(String to) {
        if(userRepository.existsByEmail(to))
            return Result.fail(409,"邮箱已被注册");
        System.out.println(to);
        if (!Utils.isEmailValid(to) || to.isEmpty()) {
            return Result.fail(400,"邮箱格式不正确");
        }

        String verificationCode = getVerificationCode(to);
        try {
            if (fromEmail == null || fromEmail.isEmpty())
                fromEmail = "1647114628@qq.com";

            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom(fromEmail);  // 发送者
            message.setTo(to);  // 接收者
            message.setSubject("您的验证码");  // 邮件主题
            message.setText("您的验证码是：" + verificationCode + "，请在5分钟内完成验证。");  // 邮件内容
            mailSender.send(message);
            return Result.success("邮件发送成功");
        }
        catch (Exception e) {
            return Result.fail("邮件发送失败" + e.getMessage());


        }
    }

    public String getVerificationCode(String email) {

        String verificationCode = Utils.generateVerificationCode();
        String key = KEY_PREFIX + email;
        redisTemplate.opsForValue().set(key, verificationCode, TIME_OUT, TimeUnit.MINUTES); // 5分钟有效期
        return verificationCode;

    }

    public Boolean VerifyCode(String email, String code) {
        String key = KEY_PREFIX + email;
        //如果验证码符合就返回true然后删除验证码
        if (code != null && code.equals(redisTemplate.opsForValue().get(key))) {
            redisTemplate.delete(key);
            return true;
        }
        return false;
    }


}
