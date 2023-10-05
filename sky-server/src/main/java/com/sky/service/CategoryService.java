package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

public interface CategoryService {
    PageResult page(CategoryPageQueryDTO categoryPageQueryDTO);

    List<Category> getByPage(Integer type);

    void addCat(CategoryDTO categoryDTO);

    void editCat(CategoryDTO categoryDTO);

    void onOrOffCat(Integer status, Long id);

    void deleteCat(Long id);
}
