package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.User;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("select * from user where openid = #{s}")
    User getByOpenID(String s);

    void insertUser(User user);

    @Select("select * from user where id = #{userId}")
    User getById(Long userId);
}
