package com.sky.service.impl;

import com.sky.entity.Category;
import com.sky.mapper.UserCategoryMapper;
import com.sky.service.UserCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @PROJECT_NAME: sky-take-out
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2023/10/10 21:01
 */
@Service
public class UserCategoryServiceImpl implements UserCategoryService {

    @Autowired
    private UserCategoryMapper userCategoryMapper;

    @Override
    public List<Category> getByCondition(Integer type) {
        List<Category> category = userCategoryMapper.getByCondition(type);
        return category;
    }
}
