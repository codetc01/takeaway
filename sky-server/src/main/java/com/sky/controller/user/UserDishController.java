package com.sky.controller.user;

import com.sky.dto.CategoryDTO;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.UserDishService;
import com.sky.vo.DishVO;
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
 * @DATE: 2023/10/10 21:22
 */
@RestController
@RequestMapping("/user/dish")
@Slf4j
@Api(tags = "菜品浏览接口")
public class UserDishController {

    @Autowired
    private UserDishService userDishService;

    @GetMapping("/list")
    @ApiOperation("根据分类Id查询菜品")
    public Result<List<DishVO>> getById(Long categoryId){
        List<DishVO> dishVO = userDishService.getByID(categoryId);
        return Result.success(dishVO);
    }
}
