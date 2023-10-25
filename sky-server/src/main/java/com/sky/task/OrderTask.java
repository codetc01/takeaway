package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @PROJECT_NAME: sky-take-out
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2023/10/23 23:22
 */
@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    @Scheduled(cron = "0 * * * * ?")
    public void processOutTimeOrder(){
        // 判断是否有订单时间超过15分钟
        log.info("执行定时任务: {}" , LocalDateTime.now());

        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(-15);

        List<Orders> ordersList = orderMapper.getByStatusAndOrderTime(Orders.PENDING_PAYMENT, localDateTime);

        if(ordersList != null && ordersList.size() > 0) {
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelTime(LocalDateTime.now());
                orders.setCancelReason("支付超时");
                orderMapper.processOutTimeOrder(orders);
            }
        }
    }

    @Scheduled(cron = "0 0 1 * * ? ")
    public void processDeliveryTime(){
        log.info("处理未送达订单： {}", LocalDateTime.now());

        LocalDateTime localDateTime = LocalDateTime.now().plusHours(-1);

        List<Orders> ordersList = orderMapper.getByStatusAndOrderTime(Orders.DELIVERY_IN_PROGRESS, localDateTime);

        if(ordersList != null && ordersList.size() > 0) {
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.COMPLETED);
                orders.setDeliveryTime(LocalDateTime.now());
                orderMapper.processDeliveryTime(orders);
            }
        }
    }
}
