package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.User;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Mapper
public interface UserMapper {

    @Select("select * from user where openid = #{s}")
    User getByOpenID(String s);

    void insertUser(User user);

    @Select("select * from user where id = #{userId}")
    User getById(Long userId);

    @Select("select count(*) from user where create_time < #{last} and create_time > #{start}")
    Integer getUserNumberInDay(LocalDateTime start, LocalDateTime last);

    @Select("select count(*) from user where create_time < #{last}")
    Integer getTotalNumber(LocalDateTime last);
}
