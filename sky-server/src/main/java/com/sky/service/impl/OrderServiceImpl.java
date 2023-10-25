package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @PROJECT_NAME: sky-take-out
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2023/10/20 20:09
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Override
    public PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        Page<OrderVO> orderVOS = orderMapper.conditionSearch(ordersPageQueryDTO);

        for (OrderVO orderVO : orderVOS.getResult()){
            String orderDishesStr = getOrderDishesStr(orderVO.getId());
            orderVO.setOrderDishes(orderDishesStr);
        }

        PageResult pageResult = new PageResult();
        pageResult.setTotal(orderVOS.getTotal());
        pageResult.setRecords(orderVOS.getResult());

        return pageResult;
    }

    @Override
    public OrderStatisticsVO statistics() {
        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        orderStatisticsVO.setToBeConfirmed(orderMapper.getByStatus(Orders.TO_BE_CONFIRMED));
        orderStatisticsVO.setConfirmed(orderMapper.getByStatus(Orders.CONFIRMED));
        orderStatisticsVO.setDeliveryInProgress(orderMapper.getByStatus(Orders.DELIVERY_IN_PROGRESS));
        return orderStatisticsVO;
    }

    @Override
    public void confirm(Long id) {
        Orders orders = new Orders();
        orders.setStatus(Orders.CONFIRMED);
        orders.setId(id);
        orderMapper.confirm(orders);
    }

    @Override
    public void delivery(Long id) {
        Orders orders = new Orders();
        orders.setId(id);
        orders.setStatus(Orders.DELIVERY_IN_PROGRESS);
        orderMapper.delivery(orders);
    }

    @Override
    public void rejection(Orders orders) {
        orders.setStatus(7);
        orders.setPayStatus(Orders.REFUND);
        orderMapper.rejection(orders);
    }

    @Override
    public void cancel(Orders orders) {
        orders.setStatus(Orders.CANCELLED);
        orders.setPayStatus(Orders.REFUND);
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.cancel(orders);
    }

    @Override
    public OrderVO details(Long id) {
        OrderVO orderVO = orderMapper.details(id);

        List<OrderDetail> byOrderId = orderDetailMapper.getByOrderId(id);
        orderVO.setOrderDetailList(byOrderId);
        return orderVO;
    }

    @Override
    public void complete(Long id) {
        Orders orders = new Orders();
        orders.setId(id);
        orders.setDeliveryTime(LocalDateTime.now());
        orders.setStatus(Orders.COMPLETED);
        orderMapper.complete(orders);
    }

    // TODO 待学习
    private String getOrderDishesStr(Long id) {
        // 查询订单菜品详情信息（订单中的菜品和数量）
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(id);

        // 将每一条订单菜品信息拼接为字符串（格式：宫保鸡丁*3；）
        List<String> orderDishList = orderDetailList.stream().map(x -> {
            String orderDish = x.getName() + "*" + x.getNumber() + ";";
            return orderDish;
        }).collect(Collectors.toList());

        // 将该订单对应的所有菜品信息拼接在一起
        return String.join("", orderDishList);
    }
}
