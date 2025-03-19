package com.fanzehao.blogsystem.config;

import com.fanzehao.blogsystem.Interceptor.JwtAuthenticationInterceptor;
import com.fanzehao.blogsystem.Interceptor.PageVisitCountInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport {
    @Autowired
    private JwtAuthenticationInterceptor jwtAuthenticationInterceptor; // 注入自定义拦截器

    @Autowired
    private PageVisitCountInterceptor pageVisitCountInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthenticationInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/login", "/api/register", "/api/sendverificationcode", "/api/tags", "/api/categories","/api/tags/all",
                        "/api/categories/counts","/api/tags/counts",
                        "/api/articles/**", "/api/comments/**", "/api/photos/**", "/api/files/**", "/api/stats");

        registry.addInterceptor(pageVisitCountInterceptor)
                .addPathPatterns("/api/articles/{id}", "/api/photos/**");
    }

    //windows
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:8081","http://localhost:8080","http://www.fzhblog.cn:8080","http://localhost") // 允许的来源域名

                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true) //是否允许发送Cookie
         .exposedHeaders("Authorization");

    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置 /photos/** 路径映射到 D:/LearnJava/photos/ 目录
        registry.addResourceHandler("/photos/**")
                .addResourceLocations("file:D:/LearnJava/photos/");

        // 配置 /img/** 路径映射到 D:/LearnJava/article_img/ 目录
        registry.addResourceHandler("/article_img/**")
                .addResourceLocations("file:D:/LearnJava/article_img/");

        registry.addResourceHandler("/temp/**")
              .addResourceLocations("file:D:/LearnJava/temp/");
    }


//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        // 配置 /photos/** 路径映射到 /home/ubuntu/photos/ 目录
//        System.out.println("配置静态资源路径");
//
//        // 配置静态资源路径，供前端访问
//        registry.addResourceHandler("/photos/**")
//                .addResourceLocations("file:/home/ubuntu/photos/");
//        registry.addResourceHandler("/article_img/**")
//                .addResourceLocations("file:/home/ubuntu/article_img/");
//        registry.addResourceHandler("/temp/**")
//                .addResourceLocations("file:/home/ubuntu/temp/");
//    }
}