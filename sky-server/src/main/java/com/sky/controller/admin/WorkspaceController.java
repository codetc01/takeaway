package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @PROJECT_NAME: sky-take-out
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2023/10/27 20:34
 */
@RestController
@RequestMapping("/admin/workspace")
@Slf4j
@Api(tags = "工作台接口")
public class WorkspaceController {

    @Autowired
    private WorkspaceService workspaceService;

    @GetMapping("/businessData")
    @ApiOperation("查询今日运营数据")
    public Result<BusinessDataVO> businessData(){
        BusinessDataVO businessDataVO = workspaceService.businessData();
        return Result.success(businessDataVO);
    }

    @GetMapping("/overviewSetmeals")
    @ApiOperation("查询套餐总览")
    public Result<SetmealOverViewVO> overviewSetmeals(){
        SetmealOverViewVO setmealOverViewVO = workspaceService.overviewSetmeals();
        return Result.success(setmealOverViewVO);
    }

    @GetMapping("/overviewDishes")
    @ApiOperation("查询菜品总览")
    public Result<DishOverViewVO> overviewDishes(){

        DishOverViewVO dishOverViewVO = workspaceService.overviewDishes();

        return Result.success(dishOverViewVO);
    }

    @GetMapping("/overviewOrders")
    @ApiOperation("c查询订单管理")
    public Result<OrderOverViewVO> overviewOrders(){

        OrderOverViewVO orderOverViewVO = workspaceService.overviewOrders();

        return Result.success(orderOverViewVO);
    }
}
