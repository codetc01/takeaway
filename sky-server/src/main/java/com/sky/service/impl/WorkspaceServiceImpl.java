package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.*;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @PROJECT_NAME: sky-take-out
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2023/10/27 20:44
 */
@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    private WorkspaceMapper workspaceMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private DishMapper dishMapper;

    @Override
    public BusinessDataVO businessData() {
        LocalDate now = LocalDate.now();

        BusinessDataVO businessDataVO = new BusinessDataVO();

        LocalDateTime start = LocalDateTime.of(now, LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(now, LocalTime.MAX);

        businessDataVO.setNewUsers(userMapper.getUserNumberInDay(start, end));

        Integer effectiveNumber = orderMapper.getEffectiveNumber(start, end, Orders.COMPLETED);
        // 已接单
        Integer effectiveNumber1 = orderMapper.getEffectiveNumber(start, end, Orders.CONFIRMED);
        //
        businessDataVO.setOrderCompletionRate((double) effectiveNumber / (effectiveNumber1 + effectiveNumber));

        Double money = orderMapper.getSaleMoney(start, end, Orders.COMPLETED);

        if (money == null) {
            money = 0.0;
        }

        businessDataVO.setTurnover(money);

        businessDataVO.setUnitPrice(money / effectiveNumber);

        businessDataVO.setValidOrderCount(effectiveNumber);

        return businessDataVO;
    }

    @Override
    public SetmealOverViewVO overviewSetmeals() {

        SetmealOverViewVO setmealOverViewVO = new SetmealOverViewVO();

        setmealOverViewVO.setDiscontinued(setmealMapper.getByStatus(0));

        setmealOverViewVO.setSold(setmealMapper.getByStatus(1));

        return setmealOverViewVO;
    }

    @Override
    public DishOverViewVO overviewDishes() {

        DishOverViewVO dishOverViewVO = new DishOverViewVO();

        dishOverViewVO.setDiscontinued(dishMapper.getByStatus(0));

        dishOverViewVO.setSold(dishMapper.getByStatus(1));

        return dishOverViewVO;
    }

    @Override
    public OrderOverViewVO overviewOrders() {

        LocalDate today = LocalDate.now();

        LocalDateTime start = LocalDateTime.of(today, LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(today, LocalTime.MAX);

        OrderOverViewVO orderOverViewVO = new OrderOverViewVO();

        orderOverViewVO.setAllOrders(orderMapper.getTotalNumber(start, end));

        orderOverViewVO.setCancelledOrders(orderMapper.getEffectiveNumber(start, end, Orders.CANCELLED));
        orderOverViewVO.setCompletedOrders(orderMapper.getEffectiveNumber(start, end, Orders.COMPLETED));
        orderOverViewVO.setDeliveredOrders(orderMapper.getEffectiveNumber(start, end, Orders.CONFIRMED));
        orderOverViewVO.setWaitingOrders(orderMapper.getEffectiveNumber(start, end, Orders.TO_BE_CONFIRMED));

        return orderOverViewVO;
    }

    @Override
    public BusinessDataVO businessData(LocalDateTime start, LocalDateTime end) {

        BusinessDataVO businessDataVO = new BusinessDataVO();

        businessDataVO.setNewUsers(userMapper.getUserNumberInDay(start, end));

        Integer effectiveNumber = orderMapper.getEffectiveNumber(start, end, Orders.COMPLETED);
        // 已接单
        Integer effectiveNumber1 = orderMapper.getEffectiveNumber(start, end, Orders.CONFIRMED);
        //
        if(effectiveNumber1 + effectiveNumber != 0) {
            businessDataVO.setOrderCompletionRate((double) effectiveNumber / (effectiveNumber1 + effectiveNumber));
        }else {
            businessDataVO.setOrderCompletionRate(0.0);
        }

        Double money = orderMapper.getSaleMoney(start, end, Orders.COMPLETED);

        if (money == null) {
            money = 0.0;
        }

        businessDataVO.setTurnover(money);

        if (effectiveNumber != 0) {
            businessDataVO.setUnitPrice(money / effectiveNumber);
        }else {
            businessDataVO.setUnitPrice(0.0);
        }

        businessDataVO.setValidOrderCount(effectiveNumber);

        return businessDataVO;
    }
}
