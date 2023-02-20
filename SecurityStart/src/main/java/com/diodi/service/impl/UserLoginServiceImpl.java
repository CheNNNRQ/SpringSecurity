package com.diodi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.diodi.bean.User;
import com.diodi.bean.UserLogin;
import com.diodi.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author diodi
 * @create 2023-02-10-11:10
 */
@Service
public class UserLoginServiceImpl implements UserDetailsService {
    @Resource
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUserName, username));
        //判断用户是否存在
        if (Objects.isNull(user)){
            throw new RuntimeException("用户不存在");
        }
        //Todo: 返回权限信息
        ArrayList<String> permissions = new ArrayList<>(Arrays.asList("test"));
        UserLogin userLogin = new UserLogin();
        userLogin.setUser(user);
        userLogin.setPermissions(permissions);
        return userLogin;
    }
}
