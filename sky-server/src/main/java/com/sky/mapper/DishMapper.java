package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DishMapper {

    @Select("select count(id) from dish where category_id = #{id}")
    Integer countByCategoryId(Long id);

    @AutoFill(OperationType.INSERT)
    void addDish(Dish build);

    Page<DishVO> page(DishPageQueryDTO dishPageQueryDTO);

    @Delete("delete from dish where id = #{id} and status = 0")
    Integer deleteDish(Long id);
}
