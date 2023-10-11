package com.sky.service;

import com.sky.vo.DishVO;

import java.util.List;

public interface UserDishService {
    List<DishVO> getByID(Long categoryId);
}
