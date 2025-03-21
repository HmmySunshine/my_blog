package com.fanzehao.blogsystem.repository;

import com.fanzehao.blogsystem.model.Article;
import com.fanzehao.blogsystem.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByArticle(Article article, Pageable pageable);

    //返回评论总数
    @Query("SELECT COUNT(c) FROM Comment c")
    Long countAllComments();
}
