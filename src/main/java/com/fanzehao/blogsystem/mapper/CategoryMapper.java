package com.fanzehao.blogsystem.mapper;

import com.fanzehao.blogsystem.model.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface CategoryMapper {
    @Select({"<script>" +
            "SELECT * FROM categories " +
            "<if test=\"name != null and name != ''\"> WHERE name LIKE CONCAT('%', #{name}, '%') </if>" +
            "</script>"})
    List<Category> findByName(@Param("name") String name);


}  