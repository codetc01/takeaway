package com.sky.controller.user;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.Result;
import com.sky.service.UserSubmitService;
import com.sky.vo.OrderSubmitVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @PROJECT_NAME: sky-take-out
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2023/10/18 16:42
 */
@RestController
@RequestMapping("/user/order")
@Slf4j
@Api(tags = "订单接口")
public class UserSubmitController {

    @Autowired
    private UserSubmitService userSubmitService;

    @PostMapping("/submit")
    @ApiOperation("用户下单")
    private Result<OrderSubmitVO> submitOrder(@RequestBody OrdersSubmitDTO ordersSubmitDTO){
        OrderSubmitVO orderSubmitVO = userSubmitService.submitOrder(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

}
