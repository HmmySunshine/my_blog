package com.fanzehao.blogsystem.Service;

import com.fanzehao.blogsystem.model.Photo;
import com.fanzehao.blogsystem.repository.PhotoRepository;
import com.fanzehao.blogsystem.response.PageResponse;
import com.fanzehao.blogsystem.response.Result;
import com.fanzehao.blogsystem.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.util.Date;

@Service
public class PhotoService {


    private final PhotoRepository photoRepository;

    public PhotoService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }
    private final static Logger logger = LoggerFactory.getLogger(PhotoService.class);
    @Value("${photo.directory.windows}")
    private String photoDirectoryWindows;

    @Value("${photo.directory.linux}")
    private String photoDirectoryLinux;

    @Value("${photo.url.windows}")
    private String photoUrlWindows;

    @Value("${photo.url.linux}")
    private String photoUrlLinux;

    private String photoDirectory;
    private String photoUrl;

    @PostConstruct
    public void init() {
        // 根据操作系统动态设置路径和 URL
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            photoDirectory = photoDirectoryWindows;
            photoUrl = photoUrlWindows;
        } else {
            photoDirectory = photoDirectoryLinux;
            photoUrl = photoUrlLinux;
        }
    }

    public Result<?> uploadPhoto(String title, String description,
                                 Boolean isWide, MultipartFile file) {
        try {
            // 保存文件到指定目录
            Utils.createFile(file, photoDirectory);

            // 保存到数据库
            Photo photo = new Photo();
            photo.setTitle(title);
            photo.setDescription(description);
            photo.setIsWide(isWide);
            photo.setCreatedAt(new Date());
            photo.setFileName(file.getOriginalFilename());
            photo.setFilePath(photoDirectory + file.getOriginalFilename());
            photo.setFileSize(file.getSize());
            photo.setFileType(file.getContentType());
            photo.setHttpUrl(photoUrl + file.getOriginalFilename()); // 使用动态 URL
            photoRepository.save(photo);

            return Result.success(photo);
        } catch (Exception e) {
            logger.error("文件上传失败: " + e.getMessage());
            return Result.fail("上传失败");
        }
    }


    public Result<?> findAll(Integer page, Integer pageSize, String title, String createdAt) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        try {
            Date date = null;
            //这个是因为前端即使发送空字符串也要查询
            if (createdAt != null && !createdAt.isEmpty()) {
                date = Utils.formatDate(createdAt);
            }

            Page<Photo> photoPage = photoRepository.findAllByPageTitleOrCreatedAt(pageable, title, date);
            PageResponse<Photo> photoPageResponse = new PageResponse<>(photoPage.getContent(), photoPage.getTotalElements());
            return Result.success(photoPageResponse);
        }
        catch (ParseException e){
            logger.error("日期格式错误"+e.getMessage());
            return Result.fail("日期格式错误");
        }
        catch (Exception e) {
            logger.error("获取所有图片失败"+e.getMessage());
            return Result.fail("获取所有图片失败");
        }

    }

    public Result<?> deletePhoto(Long fileId) {
        //先删除文件
        try {
            photoRepository.findById(fileId).ifPresent(photo -> Utils.deleteFile(photo.getFilePath()));
            //再删除数据库记录
            photoRepository.deleteById(fileId);
            return Result.success("删除成功");

        }
        catch (Exception e) {
            logger.error("删除图片失败"+e.getMessage());
            return Result.fail("删除图片失败");
        }
    }

    public Result<?> updatePhoto(Long photoId, Photo photo) {
        try {
            photoRepository.findById(photoId).ifPresent(photo1 -> {
                photo1.setTitle(photo.getTitle());
                photo1.setDescription(photo.getDescription());
                photo1.setIsWide(photo.getIsWide());
                photoRepository.save(photo1);
            });

            return Result.success("更新成功");

        }
        catch (Exception e) {
            logger.error("更新图片失败"+e.getMessage());
            return Result.fail("更新图片失败");
        }
    }
}
