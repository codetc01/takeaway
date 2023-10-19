package com.sky.service;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.vo.OrderSubmitVO;

public interface UserSubmitService {
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);
}
