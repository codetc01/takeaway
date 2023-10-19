package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.OrderBusinessException;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.mapper.UserSubmitMapper;
import com.sky.mapper.addressBookMapper;
import com.sky.service.UserSubmitService;
import com.sky.vo.OrderSubmitVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @PROJECT_NAME: sky-take-out
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2023/10/18 16:44
 */
@Service
public class UserSubmitServiceImpl implements UserSubmitService {

    @Autowired
    private UserSubmitMapper userSubmitMapper;

    @Autowired
    private addressBookMapper addressBookMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Override
    @Transactional
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        // 地址簿为空，购物车数据为空直接返回
        AddressBook addressById = addressBookMapper.getAddressById(ordersSubmitDTO.getAddressBookId());
        if(addressById == null){
            throw new OrderBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.viewShoppingCart(BaseContext.getCurrentId());
        if(shoppingCarts == null || shoppingCarts.size() == 0){
            throw new OrderBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }


        // 清空用户当前购物车
        shoppingCartMapper.cleanShoppingCart(BaseContext.getCurrentId());


        // 往order表里插入数据
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);

        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setUserId(BaseContext.getCurrentId());
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(Orders.UN_PAID);

        // 其次，联合其他表进行查询，填充相关冗余字段
        orders.setPhone(addressById.getPhone());
        orders.setAddress(addressById.getProvinceName() + addressById.getCityName() + addressById.getDistrictName() + addressById.getDetail());
        orders.setConsignee(addressById.getConsignee());

        userSubmitMapper.addData(orders);

        OrderSubmitVO orderSubmitVO = new OrderSubmitVO();
        orderSubmitVO.setId(orders.getId());
        orderSubmitVO.setOrderTime(orders.getOrderTime());
        orderSubmitVO.setOrderAmount(orders.getAmount());
        orderSubmitVO.setOrderNumber(orders.getNumber());

        // 查询购物车表，将用户的购物车数据导入order表
        OrderDetail orderDetail = new OrderDetail();
        for (ShoppingCart shoppingCart : shoppingCarts){
            BeanUtils.copyProperties(shoppingCart, orderDetail);
            orderDetail.setOrderId(orders.getId());
            orderDetailMapper.addData(orderDetail);
        }

        return orderSubmitVO;
    }
}
