package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserSubmitMapper {

    @Insert("insert into orders (number, user_id, address_book_id, order_time, checkout_time, amount, remark, phone, address, user_name, consignee, cancel_reason, rejection_reason, cancel_time, estimated_delivery_time, delivery_time, pack_amount, tableware_number) " +
            "values (#{number}, #{userId}, #{addressBookId}, #{orderTime}, #{checkoutTime}, #{amount}, #{remark}, #{phone}, #{address}, #{userName}, #{consignee}, #{cancelReason}, #{rejectionReason}, #{cancelTime}, #{estimatedDeliveryTime}, #{deliveryTime}, #{packAmount}, #{tablewareNumber})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void addData(Orders orders);

    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    @Select("select * from orders where id = #{id}")
    Orders getByID(Long id);

    @Update("update orders set status = #{cancelled} where id = #{id}")
    void cancel(Long id, Integer cancelled);
}
