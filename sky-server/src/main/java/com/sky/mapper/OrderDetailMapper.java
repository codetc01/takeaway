package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@Mapper
public interface OrderDetailMapper {

    @Insert("insert into order_detail(name, image, order_id, dish_id, setmeal_id, dish_flavor, number, amount) " +
            "values (#{name}, #{image}, #{orderId}, #{dishId}, #{setmealId}, #{dishFlavor}, #{number}, #{amount})")
    void addData(OrderDetail orderDetail);

    @Select("select * from order_detail where order_id = #{OrderId}")
    List<OrderDetail> getByOrderId(Long OrderId);

    Page<OrderVO> historyOrders(Long currentId, Integer status);

    void batchAddData(List<OrderDetail> byOrderId);
}
