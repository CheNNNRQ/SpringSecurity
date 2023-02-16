package com.diodi.service;

import com.diodi.bean.User;
import com.diodi.utils.ResponseResult;

/**
 * @author diodi
 * @create 2023-02-10-17:19
 */
public interface LoginService {

    ResponseResult login(User user);

    ResponseResult logout();
}
