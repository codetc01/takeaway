package com.sky.service.impl;

import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.UserDishMapper;
import com.sky.service.UserDishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @PROJECT_NAME: sky-take-out
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2023/10/10 21:24
 */
@Service
public class UserDishServiceImpl implements UserDishService {

    @Autowired
    private UserDishMapper userDishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Override
    public List<DishVO> getByID(Long categoryId) {
        List<DishVO> dishVO = new ArrayList<>();

        List<Dish> dishes = userDishMapper.getById(categoryId);

        for (Dish dish : dishes){
            DishVO dishVO1 = new DishVO();
            BeanUtils.copyProperties(dish, dishVO1);

            List<DishFlavor> byId1 = dishFlavorMapper.getById(dish.getId());
            dishVO1.setFlavors(byId1);
            dishVO.add(dishVO1);
        }
        return dishVO;
    }
}
