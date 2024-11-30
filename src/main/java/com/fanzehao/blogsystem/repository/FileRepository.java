package com.fanzehao.blogsystem.repository;

import com.fanzehao.blogsystem.model.FileUpload;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;



@Repository
public interface FileRepository extends JpaRepository<FileUpload, Long> {
    //根据文件名查询文件
    //select f from file_upload f where (:fileName is null or f.fileName like :fileName)

    @Query("select f from FileUpload f where (:fileName is null or f.fileName like %:fileName%)")
    Page<FileUpload> findAll(Pageable pageable, @Param("fileName") String fileName);
}
