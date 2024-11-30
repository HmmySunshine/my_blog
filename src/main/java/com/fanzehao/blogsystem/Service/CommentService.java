package com.fanzehao.blogsystem.Service;

import com.fanzehao.blogsystem.model.Article;
import com.fanzehao.blogsystem.model.Comment;
import com.fanzehao.blogsystem.pojo.User;
import com.fanzehao.blogsystem.repository.ArticleRepository;
import com.fanzehao.blogsystem.repository.CommentRepository;
import com.fanzehao.blogsystem.repository.UserRepository;
import com.fanzehao.blogsystem.response.CommentResponse;
import com.fanzehao.blogsystem.response.PageResponse;
import com.fanzehao.blogsystem.response.Result;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    public Result<?> addComment(String userId, String articleId, String content) {
        try {
            //字符串转换long
            Long userIdLong = Long.parseLong(userId);


            Long articleIdLong = Long.parseLong(articleId);

            System.out.println(articleIdLong);

            User user = userRepository.findById(userIdLong).orElseThrow(() -> new RuntimeException("user not found"));
            System.out.println(user.getId());
            Article article = articleRepository.findById(articleIdLong).orElseThrow(() -> new RuntimeException("article not found"));

            Comment comment = new Comment();
            comment.setUser(user);
            comment.setArticle(article);
            comment.setContent(content);
            commentRepository.save(comment);
        } catch (Exception e) {
            logger.error("fail to save comment", e);
            return Result.fail("fail to save comment");
        }
        return Result.success("success to save comment");
    }

    public Result<?> getComments(String articleId, Integer page, Integer pageSize) {
        try {
            Long articleIdLong = Long.parseLong(articleId);
            Pageable pageable = PageRequest.of(page - 1, pageSize);
            Article article = articleRepository.findById(articleIdLong).orElseThrow(() -> new RuntimeException("article not found"));
            Page<Comment> commentsPage = commentRepository.findByArticle(article, pageable);
            List<CommentResponse> commentResponses = commentsPage.getContent().stream().map(comment -> {
                        CommentResponse commentResponse = new CommentResponse();
                        commentResponse.setId(comment.getId());
                        commentResponse.setContent(comment.getContent());
                        commentResponse.setUsername(comment.getUser().getUsername());
                        commentResponse.setCreatedAt(comment.getCreateAt());
                        return commentResponse;
                    }).collect(Collectors.toList());
            PageResponse<CommentResponse> pageResponse = new PageResponse<>(commentResponses, commentsPage.getTotalElements());

            return Result.success(pageResponse);

        } catch (Exception e) {
            return Result.fail("fail to get comments");
        }
    }
}
