package com.fanzehao.blogsystem.Controller;

import com.fanzehao.blogsystem.Service.CategoryService;
import com.fanzehao.blogsystem.model.Category;
import com.fanzehao.blogsystem.repository.ArticleRepository;
import com.fanzehao.blogsystem.response.CategoryResponse;
import com.fanzehao.blogsystem.response.Result;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;


    @GetMapping
    public Result<?> getAllCategoriesByPage(@RequestParam(required = false) String searchName,@RequestParam(defaultValue = "0") Integer page,
                                      @RequestParam(defaultValue = "10") Integer pageSize ) {
        System.out.println(searchName);
        Page<Category> categoryByPage;
        CategoryResponse categoryResponse;
        if (searchName == null  || searchName.isEmpty()) {
            //分页查询所有
            categoryByPage = categoryService.findAllByPage(page, pageSize);
            categoryResponse = new CategoryResponse(categoryByPage.getContent(), categoryByPage.getTotalElements());
        }
        else {
            //分页查询按照名称模糊查询
            PageInfo<Category> byNamePage = categoryService.findByNamePage(searchName, page, pageSize);
            categoryResponse = new CategoryResponse(byNamePage.getList(), byNamePage.getTotal());

        }

        return Result.success(categoryResponse);
    }

    @PostMapping
    public Result<?> addCategory(@RequestBody Category category) {

        return Result.success(categoryService.addCategory(category));
    }

    //http://localhost:8090/api/categories/3
    @PutMapping("/{id}")
    public Result<?> updateCategory(@PathVariable Long id,@RequestBody Category category) {
        System.out.println(id);
        return Result.success(categoryService.updateCategory(id,category));
    }
    @DeleteMapping("/{id}")
    public Result<?> deleteCategory(@PathVariable Long id) {
        return Result.success("删除成功", categoryService.deleteCategory(id));
    }
    //根据分类获取文章数量
    @GetMapping("/counts")
   public Result<?> getCategoriesCounts() {
        Map<Long, Long> categoryCounts = categoryService.getCategoryCounts();
        return Result.success(categoryCounts);
    }

    //获取分类总数
    @GetMapping("/total")
    public Result<?> getCategoriesTotal() {
        return Result.success(categoryService.getCategoriesTotal());
    }

    //根据id获取分类名称
    @GetMapping("/name/{id}")
    public Result<?> getCategoryNameById(@PathVariable Long id) {
        return Result.success( "success",categoryService.getCategoryNameById(id));
    }
}
