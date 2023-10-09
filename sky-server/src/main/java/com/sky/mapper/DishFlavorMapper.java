package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

//    @Insert("insert into dish_flavor(dish_id, name, value) values (#{id}, #{dishFlavor}, #{dishFlavor1})")
    void addDish(List<DishFlavor> flavors);

    @Delete("delete from dish_flavor where dish_id = #{l}")
    void deleteById(Long l);

    @Select("select * from dish_flavor where dish_id = #{dishId}")
    List<DishFlavor> getById(Long dishId);

    void editDishFlavor(Long id, List<DishFlavor> flavors);
}
