package com.fanzehao.blogsystem.Service;

import com.fanzehao.blogsystem.Controller.FileController;
import com.fanzehao.blogsystem.model.FileUpload;
import com.fanzehao.blogsystem.repository.FileRepository;
import com.fanzehao.blogsystem.response.PageResponse;
import com.fanzehao.blogsystem.response.Result;
import com.fanzehao.blogsystem.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;


@Service
public class FileService {
    @Value("${upload.directory}")
    private String UPLOAD_DIRECTORY;
    private final FileRepository fileRepository;
    private final static Logger logger = LoggerFactory.getLogger(FileController.class);

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }
    public InputStream getFileInputStream(FileUpload file) throws IOException {
        return Files.newInputStream(Paths.get(file.getFilePath()));
    }

    public Result<?> uploadFile(MultipartFile file, String description) {
        if (file.isEmpty()) {
            return Result.fail("文件不能为空");
        }


        try {
            // 使用 createFile 函数上传文件
            Utils.createFile(file, UPLOAD_DIRECTORY);

            FileUpload fileUpload = new FileUpload();
            fileUpload.setFileName(file.getOriginalFilename());
            fileUpload.setFileType(file.getContentType());
            fileUpload.setFileSize(file.getSize());
            fileUpload.setDescription(description);
            fileUpload.setFilePath(UPLOAD_DIRECTORY + file.getOriginalFilename());
            fileUpload.setDownloads(0);
            fileUpload.setUploadedAt(new Date());

            fileRepository.save(fileUpload);
            logger.info("File uploaded successfully: {}", fileUpload);


        }
        catch (Exception e) {
            logger.error("文件上传失败", e);
            return Result.fail("文件上传失败" + e.getMessage());

        }
        // TODO: 实现文件上传功能
        return Result.success("File uploaded successfully");
    }


    public Result<?> deleteFile(Long fileId) {
        try {
            fileRepository.deleteById(fileId);
            return Result.success("File deleted successfully");
        }
        catch (Exception e) {
            return Result.fail("File deletion failed: " + e.getMessage());
        }
    }

    public Result<?> getAllFiles(Integer page, Integer pageSize, String search) {
        //因为是从0开始的，所以要减1
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        try {
            Page<FileUpload> pageResult;
            if (search != null && !search.isEmpty()) {
              pageResult = fileRepository.findAll(pageable, search);

            }
            else {
             pageResult = fileRepository.findAll(pageable);
            }

            PageResponse<FileUpload> pageResponse = new PageResponse<>(pageResult.getContent(), pageResult.getTotalElements());
            return Result.success(pageResponse);
        }

        catch (Exception e) {
            logger.error("文件获取失败", e);
            return Result.fail("File retrieval failed: " + e.getMessage());
        }
    }

    public ResponseEntity<InputStreamResource> downloadFile(Long fileId) {
        try {
            FileUpload fileUpload = fileRepository.findById(fileId).orElse(null);
            if (fileUpload == null) {
                return ResponseEntity.notFound().build();
            }
            InputStream fileInputStream = getFileInputStream(fileUpload);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileUpload.getFileName());
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
            InputStreamResource inputStreamResource = new InputStreamResource(fileInputStream);
            return ResponseEntity.ok().headers(headers).body(inputStreamResource);

        }
        catch (Exception e) {
            logger.error("文件下载失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    public Result<?> updateDownloadsCount(Long fileId) {
        try {
            FileUpload fileUpload = fileRepository.findById(fileId).orElse(null);
            if (fileUpload == null) {
                return Result.fail("File not found");
            }
            fileUpload.setDownloads(fileUpload.getDownloads() + 1);
            fileRepository.save(fileUpload);
            return Result.success("Download count updated successfully");
        }
        catch (Exception e) {
            logger.error("文件下载次数更新失败", e);
            return Result.fail("Failed to update download count: " + e.getMessage());
        }
    }
}
