package com.fanzehao.blogsystem.util;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Random;

public class Utils {
    // 密码加密 加密方式为BCrypt


     public static void  createFile(MultipartFile file, String uploadDirectory) throws IOException, IllegalAccessException {
        // 获取文件名
        try {
            // 获取文件名
            String originalFilename = file.getOriginalFilename();
            File uploadDir = new File(uploadDirectory);
            if (!uploadDir.exists()) {
                if (!uploadDir.mkdirs()) {
                    throw new IllegalAccessException("无法创建上传目录");
                }
            }
            String filePath = uploadDirectory + originalFilename;
            File dest = new File(filePath);
            file.transferTo(dest);
        } catch (IOException e) {
            throw new IOException("文件保存失败", e);
        }
    }


    public static String hashPassword(String password) {
        String salt = BCrypt.gensalt();
        return BCrypt.hashpw(password, salt);
    }
    public static boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
    // 时间戳格式化
    public static Date parseStringToTimeStamp(String timestamp) throws ParseException {

        //2024-11-19 15:02:02
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.parse(timestamp);
    }
    public static Date formatDate(String dateStr) throws ParseException {
         String dateFormat = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        return simpleDateFormat.parse(dateStr);
    }



    public static String  generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 生成100000-999999之间的随机数
        return String.valueOf(code);
    }

    //用户名是否合法
    public static boolean isUsernameValid(String username) {
        return username.matches("^[a-zA-Z0-9_]{3,16}$");//用户名只能包含字母、数字和下划线，长度在3到16之间
    }
    //验证邮箱是否合法
    public static boolean isEmailValid(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");//邮箱格式为：字母、数字、下划线@字母、数字、下划线.字母、数字、下划线
    }

    public static boolean isPasswordValid(String password) {
        //密码可以数字大小写字母下划线和.长度8到16之间
        return password.matches("^[a-zA-Z0-9._]{6,16}$");
    }
    public static void deleteFile(String filePath) {
         //删除文件

    }
}
