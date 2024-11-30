package com.fanzehao.blogsystem;

import com.fanzehao.blogsystem.util.Utils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;

@SpringBootTest
class BlogSystemApplicationTests {
    private final static String UPLOAD_DIRECTORY = "uploads/";

    @Test
    void contextLoads() {
        File f = new File(UPLOAD_DIRECTORY);
        // 尝试创建目录
        if (!f.exists()) {
            f.mkdirs();
        }
        // 创建一个测试文件
        File testFile = new File(f, "test.txt");
        try {
            if (testFile.createNewFile()) {
                System.out.println("文件创建成功: " + testFile.getAbsolutePath());
            } else {
                System.out.println("文件已存在: " + testFile.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
