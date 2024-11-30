package com.fanzehao.blogsystem.Controller;


import com.fanzehao.blogsystem.util.Utils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@RestController
public class ImagesController {

    private final static Logger logger = LoggerFactory.getLogger(ImagesController.class);


    //{
    //  "errno": 0,
    //  "data": [
    //    {
    //      "url": "http://服务器地址/图片文件名.jpg"
    //    }
    //  ]
    //}

    @Value("${temp.directory}")
    private String tempDirectory;
    @RequestMapping("/api/image")
    @ResponseBody
    public ResponseEntity<?> uploadImage(@RequestHeader(name = "Authorization", required = false) String authorization, @RequestParam("file") MultipartFile file) {
        try {
            // 验证认证令牌
            if (authorization == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new UploadResult(1, "未授权访问"));
            }

            Utils.createFile(file, tempDirectory);
            String imageUrl = "http://localhost:8090/temp/" + file.getOriginalFilename();
            return ResponseEntity.ok(new UploadResult(0, imageUrl));
        } catch (Exception e) {
            logger.error("上传图片失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new UploadResult(1, "上传图片失败"));
        }
    }

    @Data
    private static class UploadResult {
        private int errno;
        private List<ImageInfo> data;

        public UploadResult(int errno, String url) {
            this.errno = errno;
            this.data = Collections.singletonList(new ImageInfo(url));
        }
        @Data
        private static class ImageInfo {
            private String url;

            public ImageInfo(String url) {
                this.url = url;
            }
        }
    }
}
