package com.sky.mapper;

import com.sky.entity.OrderDetail;
import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDetailMapper {

    @Insert("insert into order_detail(name, image, order_id, dish_id, setmeal_id, dish_flavor, number, amount) " +
            "values (#{name}, #{image}, #{orderId}, #{dishId}, #{setmealId}, #{dishFlavor}, #{number}, #{amount})")
    void addData(OrderDetail orderDetail);

}
