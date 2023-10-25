package com.sky.service;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;

public interface OrderService {
    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    OrderStatisticsVO statistics();

    void confirm(Long id);

    void delivery(Long id);

    void rejection(Orders orders);

    void cancel(Orders orders);

    OrderVO details(Long id);

    void complete(Long id);
}
