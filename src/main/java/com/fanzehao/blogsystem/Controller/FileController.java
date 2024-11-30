package com.fanzehao.blogsystem.Controller;

import com.fanzehao.blogsystem.Service.FileService;
import com.fanzehao.blogsystem.response.Result;
import org.apache.ibatis.annotations.Update;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/files")
public class FileController {


    private final FileService fileService;


    public FileController(FileService fileService) {
        this.fileService = fileService;
    }



    @GetMapping("/getAllFiles")
    public Result<?> getAllFiles(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                 @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                 @RequestParam(value = "keyword", defaultValue = "") String search) {
        return fileService.getAllFiles(page, pageSize, search);

    }

    @PostMapping("/upload")
    public Result<?> uploadFile(@RequestParam("file") MultipartFile file,
            @RequestParam("description") String description) {
        return fileService.uploadFile(file, description);
    }
    @DeleteMapping("/{fileId}")
    public Result<?> deleteFile(@PathVariable Long fileId) {
        return fileService.deleteFile(fileId);
    }

    @GetMapping("/download/{fileId}")
    ResponseEntity<InputStreamResource> downloadFile(@PathVariable Long fileId) {
        return fileService.downloadFile(fileId);
    }

    @PutMapping("/updateDownloadsCount/{fileId}")
    Result<?> updateDownloadsCount(@PathVariable Long fileId) {
        return fileService.updateDownloadsCount(fileId);
    }
}
