package com.sky.service.impl;

import com.sky.service.ShopService;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @PROJECT_NAME: sky-take-out
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2023/10/9 20:37
 */
@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void editShopStatus(Integer status) {
        redisTemplate.opsForValue().set("SHOP_STATUS", status);
    }

    @Override
    public Integer selectStatus() {
        Integer shop_status = (Integer) redisTemplate.opsForValue().get("SHOP_STATUS");
        return shop_status;
    }
}
