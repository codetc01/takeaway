package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ShopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @PROJECT_NAME: sky-take-out
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2023/10/9 20:36
 */
@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Slf4j
@Api(tags = "店铺操作接口")
public class ShopController {

    @Autowired
    private ShopService shopService;

    @PutMapping("/{status}")
    @ApiOperation("修改店铺状态")
    public Result editShopStatus(@PathVariable Integer status){
        shopService.editShopStatus(status);
        return Result.success();
    }

    @GetMapping("/status")
    @ApiOperation("查询店铺状态")
    public Result<Integer> selectStatus(){
        Integer status = shopService.selectStatus();
        return Result.success(status);
    }
}
