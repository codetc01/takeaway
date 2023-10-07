package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SetmealDishMapper {

    @Select("select * from setmeal_dish where dish_id = #{l};")
    SetmealDish getById(Long l);
}
