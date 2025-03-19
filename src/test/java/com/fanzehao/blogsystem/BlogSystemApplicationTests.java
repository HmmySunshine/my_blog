package com.fanzehao.blogsystem;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.fanzehao.blogsystem.model.Article;
import com.fanzehao.blogsystem.repository.ArticleRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.List;

@SpringBootTest
class BlogSystemApplicationTests {
    private final static String UPLOAD_DIRECTORY = "uploads/";

    //生成一百个用户进行测试
    @Test
    void testGenerateUsers() {
        BCryptPasswordEncoder brcyptPasswordEncoder = new BCryptPasswordEncoder();
        for (int i = 1; i <= 100; i++) {
            String rawPassword = "123456"; // 测试密码
            String encodedPassword = brcyptPasswordEncoder.encode(rawPassword);
            System.out.printf("('user%d', 'test%d@example.com', '%s'),%n",
                    i,  i, encodedPassword);
        }
    }

    @Test
    void testUserCsv() {
        System.out.println("username,password");
        for (int i = 1; i <= 100; i++) {
            System.out.println("user" + i + ",123456");
        }
    }



    @Autowired
    private ArticleRepository articleRepository;
    @Test
    void testArticleViewOrder() {
        List<Article> top10ByOrderByViewsDesc = articleRepository.findTop10ByOrderByViewsDesc();
        for (Article article : top10ByOrderByViewsDesc) {
            System.out.println(article.getId());
        }
    }

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
