package com.sky.service;

import com.sky.vo.UserLoginVO;

public interface UserService {
    UserLoginVO userLogin(String code);
}
