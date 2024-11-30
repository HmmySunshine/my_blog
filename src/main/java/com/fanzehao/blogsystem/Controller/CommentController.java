package com.fanzehao.blogsystem.Controller;

import com.fanzehao.blogsystem.Service.CommentService;
import com.fanzehao.blogsystem.pojo.CommentDTO;
import com.fanzehao.blogsystem.response.Result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping()
    public Result<?> addComment(@RequestBody CommentDTO commentDTO) {
        //校验评论内容是否为空

       if (commentDTO.getContent() == null || commentDTO.getContent().isEmpty()) {
           return Result.fail("评论内容不能为空");
       }
       //校验用户是否登录
       if (commentDTO.getUserId() == null) {
           return Result.fail("用户未登录");
       }
       //校验文章是否存在
       if (commentDTO.getArticleId() == null) {
           return Result.fail("文章不存在");
       }
        return commentService.addComment(commentDTO.getUserId(), commentDTO.getArticleId(), commentDTO.getContent());
    }


    @GetMapping("/{articleId}")
    public Result<?> getComments(@PathVariable String articleId, @RequestParam(value = "page", defaultValue = "1") Integer page,
                                 @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        return commentService.getComments(articleId, page, pageSize);
    }
}
