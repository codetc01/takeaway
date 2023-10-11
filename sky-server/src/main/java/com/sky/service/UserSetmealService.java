package com.sky.service;

import com.sky.entity.Setmeal;

import java.util.List;

public interface UserSetmealService {
    List<Setmeal> getById(Long categoryId);
}
