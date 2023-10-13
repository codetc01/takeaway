package com.sky.service.impl;

import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.service.UserSetmealService;
import com.sky.vo.DishItemVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @PROJECT_NAME: sky-take-out
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2023/10/10 22:11
 */
@Service
public class UserSetmealServiceImpl implements UserSetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private DishMapper dishMapper;

    @Override
    public List<Setmeal> getById(Long categoryId) {
        List<Setmeal> setmeals = setmealMapper.getByCategoryId(categoryId);
        return setmeals;
    }

    @Override
    public List<DishItemVO> getBySetmealId(Long id) {
        // 先查询setmeal_dish表，获取菜品列表
        List<SetmealDish> setmealDishes = setmealDishMapper.getByCategoryId(id);
        // 查询dish表，封装数据
        List<DishItemVO> dishItemVOList = new ArrayList<>();
        for (SetmealDish setmealDish : setmealDishes){
            DishItemVO dishItemVO = new DishItemVO();
            Dish byId = dishMapper.getById(setmealDish.getDishId());
            BeanUtils.copyProperties(byId, dishItemVO);
            dishItemVO.setCopies(setmealDish.getCopies());
            dishItemVOList.add(dishItemVO);
        }
        return dishItemVOList;
    }
}
