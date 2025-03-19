package com.fanzehao.blogsystem.repository;

import com.fanzehao.blogsystem.model.Tag;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByName(String name);
    @Query("SELECT t FROM Tag t WHERE (:name IS NULL OR t.name LIKE %:name%)")
    Page<Tag> findPaginatedTagsByName(@Param("name") String name , Pageable pageable);

    ;
}
