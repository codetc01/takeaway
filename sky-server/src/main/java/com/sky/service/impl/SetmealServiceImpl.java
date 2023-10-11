package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @PROJECT_NAME: sky-take-out
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2023/10/10 22:41
 */
@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private DishMapper dishMapper;

    @Override
    public PageResult page(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());

        Page<SetmealVO> page = setmealMapper.page(setmealPageQueryDTO);

        PageResult pageResult = new PageResult();
        System.out.println(page.getTotal());
        System.out.println(page.getResult());
        pageResult.setTotal(page.getTotal());
        pageResult.setRecords(page.getResult());

        return pageResult;
    }

    @Override
    @Transactional
    public void addSetmeal(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        // 先在setmeal表中插入数据
        setmealMapper.addSetmealWithSetmalDish(setmeal);

        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes){
            log.info("info" + setmealDish.getSetmealId());
            setmealDish.setSetmealId(setmeal.getId());
            setmealDishMapper.addSetmealWithSetmalDish(setmealDish);
        }

    }

    @Override
    public void editStatus(Integer status, Long id) {
//        Dish byId = dishMapper.getById(id);
//        System.out.println(byId.getStatus());
//        if(byId.getStatus() == 0){
//            throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
//        }
        // 先要查询当前分类下的菜品
        List<SetmealDish> setmealDishes = setmealDishMapper.getByCategoryId(id);
        for (SetmealDish setmealDish : setmealDishes){
            Dish byId = dishMapper.getById(setmealDish.getDishId());
            if(byId.getStatus() == 0){
                throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
            }
        }
        setmealMapper.editStatus(status, id);
    }

    @Override
    public SetmealDTO getById(Long id) {
        Setmeal setmeal = setmealMapper.getById(id);
        SetmealDTO setmealDTO = new SetmealDTO();
        BeanUtils.copyProperties(setmeal, setmealDTO);

        List<SetmealDish> setmealDishes = setmealDishMapper.getByCategoryId(setmeal.getId());
        setmealDTO.setSetmealDishes(setmealDishes);
        return setmealDTO;
    }

    @Override
    @Transactional
    public void editSetmeal(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.editSetmeal(setmeal);

        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        // 删除SetmealDish中关联的数据
        setmealDishMapper.deleteBySetmealId(setmealDTO.getId());

        // 循环添加
        for (SetmealDish setmealDish : setmealDishes){
            setmealDish.setSetmealId(setmealDTO.getId());
            setmealDishMapper.addSetmealWithSetmalDish(setmealDish);
        }
    }

    @Override
    public void deleteSetmeal(List<Long> ids) {
        // 先查询，如果底下有关联的菜品，则不能删除
//        for (Long id : ids){
//            List<SetmealDish> byCategoryId = setmealDishMapper.getByCategoryId(id);
//            if(byCategoryId != null || byCategoryId.size() != 0){
//                throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
//            }
//        }

        // 查询套餐是否启售
        for (Long id : ids){
            Setmeal byId = setmealMapper.getById(id);
            if (byId.getStatus() == 1){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }

        setmealMapper.deleteSetmeal(ids);

        // 还要删除setmealDish表
        for (Long id : ids) {
            setmealDishMapper.deleteBySetmealId(id);
        }
    }
}
