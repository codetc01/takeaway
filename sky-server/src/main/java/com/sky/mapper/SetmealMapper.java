package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface SetmealMapper {
    @Select("select count(id) from setmeal where category_id = #{id}")
    Integer countByCategoryId(Long id);

    Page<SetmealVO> page(SetmealPageQueryDTO setmealPageQueryDTO);

    @AutoFill(value = OperationType.INSERT)
    void addSetmealWithSetmalDish(Setmeal setmeal);

    @Update("update setmeal set status = #{status} where id = #{id}")
    void editStatus(Integer status, Long id);

    @Select("select * from setmeal where id = #{id}")
    Setmeal getById(Long id);

    @AutoFill(value = OperationType.UPDATE)
    void editSetmeal(Setmeal setmeal);

    void deleteSetmeal(List<Long> ids);

    @Select("select * from setmeal where category_id = #{categoryId}")
    List<Setmeal> getByCategoryId(Long categoryId);

    @Select("select count(*) from setmeal where status = #{status}")
    Integer getByStatus(Integer status);

    @Select("select * from setmeal where category_id = #{categoryId} and status = #{i}")
    List<Setmeal> getByCategoryIdAndStatus(Long categoryId, int i);
}
