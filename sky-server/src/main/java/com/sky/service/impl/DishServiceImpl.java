package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @PROJECT_NAME: sky-take-out
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2023/10/6 16:17
 */
@Service
@Transactional
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;


    @Override
    public void addDish(DishDTO dishDTO) {
        Dish dish = new Dish();
        dish.setName(dishDTO.getName());
        dish.setCategoryId(dishDTO.getCategoryId());
        dish.setDescription(dishDTO.getDescription());
        dish.setImage(dishDTO.getImage());
        dish.setPrice(dishDTO.getPrice());
        dish.setStatus(dishDTO.getStatus());

        dishMapper.addDish(dish);

        Long id = dish.getId();

        log.info("输出", id);

        List<DishFlavor> flavors = dishDTO.getFlavors();
        for(DishFlavor dishFlavor : flavors){
            dishFlavor.setDishId(id);
        }

        dishFlavorMapper.addDish(flavors);

    }

    @Override
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {
        // 先进性分页相关操作
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());

        Page<DishVO> page = dishMapper.page(dishPageQueryDTO);

        PageResult pageResult = new PageResult();
        pageResult.setTotal(page.getTotal());
        pageResult.setRecords(page.getResult());

        // TODO 多表查询
        return pageResult;
    }

    @Override
    public void deleteDish(String ids) {
        // 先对字符串进行解析，获取相关数组
        String[] split = ids.split(",");
        if(split.length > 0 && split != null) {
            for (String id : split) {
                Long l = Long.parseLong(id);

                SetmealDish byId = setmealDishMapper.getById(l);
                if(byId != null){
                    log.info("该菜品被套餐关联，无法删除!" +byId);
                    throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
                }

                Integer integer = dishMapper.deleteDish(l);
                // 正在启售，无法删除
                if(integer == 0){
                    log.info("正在启售，无法删除" + integer);
                    throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
                }

                dishFlavorMapper.deleteById(l);
            }
        }
    }
}
