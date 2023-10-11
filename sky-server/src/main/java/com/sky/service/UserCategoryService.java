package com.sky.service;

import com.sky.entity.Category;

import java.util.List;

public interface UserCategoryService {
    List<Category> getByCondition(Integer type);
}
