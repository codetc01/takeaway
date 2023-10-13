package com.sky.service;

import com.sky.entity.Setmeal;
import com.sky.vo.DishItemVO;

import java.util.List;

public interface UserSetmealService {
    List<Setmeal> getById(Long categoryId);

    List<DishItemVO> getBySetmealId(Long id);
}
