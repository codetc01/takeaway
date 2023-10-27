package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import com.sky.vo.Top10VO;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface DishMapper {

    @Select("select count(id) from dish where category_id = #{id}")
    Integer countByCategoryId(Long id);

    @AutoFill(OperationType.INSERT)
    void addDish(Dish build);

    Page<DishVO> page(DishPageQueryDTO dishPageQueryDTO);

    @Delete("delete from dish where id = #{id} and status = 0")
    Integer deleteDish(Long id);

    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);

    @AutoFill(value = OperationType.UPDATE)
    void editDish(Dish dish);

    @Select("select * from dish where category_id = #{id}")
    List<Dish> getByCategoryId(Long id);

    @Update("update dish set status = #{status}")
    void editDishStatus(Integer status);

}
