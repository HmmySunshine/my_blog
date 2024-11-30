package com.fanzehao.blogsystem.mapper;

import com.fanzehao.blogsystem.pojo.User;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    //查询
    @Select("select * from users where username=#{username}")
    User selectUser(@Param("username") String username);



}
