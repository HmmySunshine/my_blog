package com.fanzehao.blogsystem.Service;

import com.fanzehao.blogsystem.mapper.CategoryMapper;
import com.fanzehao.blogsystem.model.Category;
import com.fanzehao.blogsystem.repository.CategoryRepository;
import com.github.pagehelper.PageHelper;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryMapper categoryMapper;
    public Page<Category> findAllByPage(Integer page, Integer pageSize) {
        try {
            Pageable pageable = PageRequest.of(page, pageSize);
            return categoryRepository.findAll( pageable);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    //TODO: 分类服务
    public List<Category> getCategoryList() {
        try {
            return categoryRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Category findById(Long id) {
        try {
            Category category = categoryRepository.findById(id).get();
            return category;
        } catch (Exception e)  {
            e.printStackTrace();
            return null;
        }
    }

    public Category addCategory(Category category) {
        try {
            return categoryRepository.save(category);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
    public PageInfo<Category> findByNamePage(String name, Integer page, Integer pageSize) {
        try {
            PageHelper.startPage(page, pageSize);
            List<Category> byName = categoryMapper.findByName(name);
            return new PageInfo<>(byName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public Category updateCategory(Long id, Category category) {
        try {
            Category category1 = categoryRepository.findById(id).get();
            category1.setName(category.getName());
            return categoryRepository.save(category1);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Category deleteCategory(Long id) {
        try {
            Category category = categoryRepository.findById(id).get();
            categoryRepository.delete(category);

            return category;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
