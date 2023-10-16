package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @PROJECT_NAME: sky-take-out
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2023/10/15 20:51
 */
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        // 先查询购物车，如果购物车中已经有了该数据，直接修改数量即可

        ShoppingCart byDishIdAndSetmealId = shoppingCartMapper.getByDishIdAndSetmealId(shoppingCart);
        if(byDishIdAndSetmealId != null){
            shoppingCart.setNumber(byDishIdAndSetmealId.getNumber() + 1);
            shoppingCart.setId(byDishIdAndSetmealId.getId());
            shoppingCartMapper.updateShoppingCart(shoppingCart);
            return;
        }

        // 加入的是菜品ID
        if(shoppingCartDTO.getDishId() != null){
            Dish byId = dishMapper.getById(shoppingCartDTO.getDishId());
            shoppingCart.setName(byId.getName());
            shoppingCart.setNumber(1);
            shoppingCart.setAmount(byId.getPrice());
            shoppingCart.setImage(byId.getImage());
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCart.setUserId(BaseContext.getCurrentId());
        }

        if(shoppingCartDTO.getSetmealId() != null){
            Setmeal byId = setmealMapper.getById(shoppingCartDTO.getSetmealId());
            shoppingCart.setName(byId.getName());
            shoppingCart.setNumber(1);
            shoppingCart.setAmount(byId.getPrice());
            shoppingCart.setImage(byId.getImage());
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCart.setUserId(BaseContext.getCurrentId());
        }

        shoppingCartMapper.addShoppingCart(shoppingCart);
    }

    @Override
    public List<ShoppingCart> viewShoppingCart() {
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.viewShoppingCart(BaseContext.getCurrentId());
        return shoppingCarts;
    }
}
