package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.sky.constant.MessageConstant;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import com.sky.vo.UserLoginVO;
import org.apache.http.client.methods.HttpGet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @PROJECT_NAME: sky-take-out
 * @DESCRIPTION:
 * @USER: Administrator
 * @DATE: 2023/10/10 11:09
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserLoginVO userLogin(String code) {
        String openid = getOpenid(code);

        if(openid == null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }


        // 查询是否存在该用户
        User user;
        user =  userMapper.getByOpenID(openid);
        if(user == null){
            // 直接Insert
            user = new User();
            user.setOpenid(openid);
            user.setCreateTime(LocalDateTime.now());
            userMapper.insertUser(user);
        }
        UserLoginVO userLoginVO = new UserLoginVO();
        userLoginVO.setOpenid(user.getOpenid());
        userLoginVO.setId(user.getId());
        return userLoginVO;
    }

    private String getOpenid(String code) {
        // 先建立一个Map集合，存放所需的信息
        Map<String, String> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("appid", weChatProperties.getAppid());
        objectObjectHashMap.put("secret", weChatProperties.getSecret());
        objectObjectHashMap.put("js_code", code);
        objectObjectHashMap.put("grant_type", "authorization_code");

        // 先在这里发送http请求，获取用户唯一标识openID
        String s = HttpClientUtil.doGet("https://api.weixin.qq.com/sns/jscode2session", objectObjectHashMap);
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            JsonNode rootNode = objectMapper.readTree(s);
//            openid = rootNode.get("openid").asText();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        JSONObject jsonObject = JSON.parseObject(s);
        String openid = jsonObject.getString("openid");
        return openid;
    }
}
