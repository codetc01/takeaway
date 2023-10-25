package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.PutMapping;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {
    Page<OrderVO> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    @Select("select count(*) from orders where status = #{status}")
    Integer getByStatus(Integer status);

    @Update("update orders set status = #{status} where id = #{id}")
    void confirm(Orders orders);

    @Update("update orders set status = #{status} where id = #{id}")
    void delivery(Orders orders);

    @Update("update orders set status = #{status}, pay_status = #{payStatus}, rejection_reason = #{rejectionReason} where id = #{id}")
    void rejection(Orders orders);

    @Update("update orders set status = #{status}, pay_status = #{payStatus}, cancel_reason = #{cancelReason}, cancel_time = #{cancelTime} where id = #{id}")
    void cancel(Orders orders);

    @Select("select * from orders where id = #{id}")
    OrderVO details(Long id);

    @Update("update orders set delivery_time = #{deliveryTime}, status = #{status} where id = #{id}")
    void complete(Orders orders);

    @Select("select * from orders where status = #{unPaid} and order_time < #{localDateTime}")
    List<Orders> getByStatusAndOrderTime(Integer unPaid, LocalDateTime localDateTime);

    @Update("update orders set status = #{status}, cancel_reason = #{cancelReason}, cancel_time = #{cancelTime} where id = #{id}")
    void processOutTimeOrder(Orders orders);

    @Update("update orders set status = #{status}, delivery_time = #{deliveryTime} where id = #{id}")
    void processDeliveryTime(Orders orders);
}
