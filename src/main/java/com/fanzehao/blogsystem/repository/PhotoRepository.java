package com.fanzehao.blogsystem.repository;

import com.fanzehao.blogsystem.model.Photo;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {


    @Query("select p from Photo p " +
            "where (:title is null or p.title  like %:title%) " +
            "and (:createdAt is null or FUNCTION('DATE', p.createdAt) = :createdAt)")

    Page<Photo> findAllByPageTitleOrCreatedAt(Pageable pageable,
                                                     @Param("title") String title,
                                                     @Param("createdAt") Date createdAt);
}
