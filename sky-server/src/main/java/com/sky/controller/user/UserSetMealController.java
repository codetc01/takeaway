package com.sky.controller.user;

import com.sky.entity.Setmeal;
import com.sky.result.Result;
import com.sky.service.UserSetmealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @PROJECT_NAME: sky-take-out
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2023/10/10 22:09
 */
@RestController
@RequestMapping("/user/setmeal")
@Slf4j
@Api(tags = "套餐浏览接口")
public class UserSetMealController {

    @Autowired
    private UserSetmealService userSetmealService;

    @GetMapping("/list")
    @ApiOperation("根据id查询套餐")
    public Result<List<Setmeal>> getById(Long categoryId){
        List<Setmeal> setmeals =  userSetmealService.getById(categoryId);
        return Result.success(setmeals);
    }
}
