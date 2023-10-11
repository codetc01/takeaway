package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;

import java.util.List;

public interface SetmealService {
    PageResult page(SetmealPageQueryDTO setmealPageQueryDTO);

    void addSetmeal(SetmealDTO setmealDTO);

    void editStatus(Integer status, Long id);

    SetmealDTO getById(Long id);

    void editSetmeal(SetmealDTO setmealDTO);

    void deleteSetmeal(List<Long> ids);
}
