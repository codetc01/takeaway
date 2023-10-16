package com.sky.mapper;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @PROJECT_NAME: sky-take-out
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2023/10/15 20:52
 */
@Mapper
public interface ShoppingCartMapper {

    @Insert("insert into shopping_cart(name, image, user_id, dish_id, setmeal_id, dish_flavor, number, amount, create_time) values (#{name}, #{image}, #{userId}, #{dishId}, #{setmealId}, #{dishFlavor}, #{number}, #{amount}, #{createTime})")
    void addShoppingCart(ShoppingCart shoppingCart);

    ShoppingCart getByDishIdAndSetmealId(ShoppingCart shoppingCart);

    void updateShoppingCart(ShoppingCart shoppingCart);

    @Select("select * from shopping_cart where user_id = #{currentId}")
    List<ShoppingCart> viewShoppingCart(Long currentId);
}
