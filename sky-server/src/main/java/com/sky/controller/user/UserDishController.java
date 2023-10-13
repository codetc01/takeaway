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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
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

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/list")
    @ApiOperation("根据分类Id查询菜品")
    public Result<List<DishVO>> getById(Long categoryId){

        ValueOperations valueOperations = redisTemplate.opsForValue();
        List<DishVO> dishVO = (List<DishVO>) valueOperations.get("dish_" + categoryId);

        if(dishVO != null && dishVO.size() > 0){
            return Result.success(dishVO);
        }


        dishVO = userDishService.getByID(categoryId);

        // 如果缓存没有，查询出数据后传递到缓存中
        valueOperations.set("dish_" + categoryId, dishVO);

        return Result.success(dishVO);
    }
}
