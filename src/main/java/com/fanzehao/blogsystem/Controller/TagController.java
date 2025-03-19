package com.fanzehao.blogsystem.Controller;

import com.fanzehao.blogsystem.Service.TagService;
import com.fanzehao.blogsystem.model.Tag;
import com.fanzehao.blogsystem.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/tags")
public class TagController {
    //TODO: 实现标签管理功能
    @Autowired
    private TagService tagService;

    //分页获取标签
    @GetMapping()
    public Result<?> getAllTags(@RequestParam(value = "name",required = false) String name,
                                @RequestParam(value = "page", defaultValue = "1") Integer page,
                                @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize){

        return tagService.getTagsByPage(name, page, pageSize);

    }

    //获取所有标签
    @GetMapping("/all")
    public Result<?> getAllTags(){
    return tagService.getAllTags();

    }

//    @GetMapping()
//    public Result<?> getAllTags(){
//        return tagService.getAllTags();
//    }
    @PostMapping()
    public Result<?> addTag(@RequestBody Tag tag){
        return tagService.addTag(tag);
    }
    @PutMapping("/{id}")
    public Result<?> updateTag(@PathVariable Long id, @RequestBody Tag tag){
        return tagService.updateTag(id, tag);
    }

    @DeleteMapping("/{id}")
    public Result<?> deleteTag(@PathVariable Long id){

        return tagService.deleteTag(id);
    }

    @GetMapping("/counts")
    public Result<?> getTagCounts(){
        return Result.success(tagService.getTagCounts());
    }

}
