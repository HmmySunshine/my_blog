package com.fanzehao.blogsystem.Controller;

import com.fanzehao.blogsystem.Service.PhotoService;
import com.fanzehao.blogsystem.model.Photo;
import com.fanzehao.blogsystem.response.Result;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/photos")
public class PhotoController {

    private final PhotoService photoService;


    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }
    //page: 1
    //pageSize: 10
    //title:
    //createdAt: 2024-11-18
    @GetMapping()
    public Result<?> findAll(@RequestParam(value = "page", defaultValue = "1") Integer page,
                             @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                             @RequestParam(value = "title", required = false) String title,
                             @RequestParam(value = "createdAt", required = false) String createdAt) {
        System.out.println(page + " " + pageSize + " " + title + " " + createdAt);
        return photoService.findAll(page, pageSize, title, createdAt);


    }

    // 上传图片
    @PostMapping
    public Result<?> photo(@RequestParam("title") String title,
                               @RequestParam("description") String description,
                               @RequestParam("isWide") Boolean isWide,
                               @RequestParam("file") MultipartFile file) {
        return photoService.uploadPhoto(title, description, isWide, file);
    }

    @DeleteMapping("/{photoId}")
    public Result<?> deletePhoto(@PathVariable Long photoId) {
        return photoService.deletePhoto(photoId);
    }

    @PutMapping("/{photoId}")
    public Result<?> updatePhoto(@PathVariable Long photoId, @RequestBody Photo photo) {
        return photoService.updatePhoto(photoId, photo);
    }
}
