package com.diodi.service.impl;

import com.diodi.bean.User;
import com.diodi.bean.UserLogin;
import com.diodi.service.LoginService;
import com.diodi.utils.JwtUtil;
import com.diodi.utils.RedisCache;
import com.diodi.utils.ResponseResult;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author diodi
 * @create 2023-02-10-17:24
 */
@Service
public class LoginServiceImpl implements LoginService {
    @Resource
    AuthenticationManager authenticationManager;
    @Resource
    RedisCache redisCache;
    @Override
    public ResponseResult login(User user) {
        //调用AuthenticationManager的authenticate方法来进行用户认证
        //调用之前需要在securityConfig中重写authenticationManagerBean方法
        //authenticate方法的参数是一个Authentication对象 现在需要new一个UsernamePasswordAuthenticationToken对象
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //如果认证失败 authenticate为空 直接返回结果
        if (Objects.isNull(authenticate)){
            return new ResponseResult(500,"认证失败");
        }
        //如果认证成功 从authenticate中获取用户信息 获取到的是实现UserDetails的UserLogin对象
        UserLogin principal = (UserLogin) authenticate.getPrincipal();
        //根据用户信息生成token
        String userId = principal.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        HashMap<String, String> map = new HashMap<>();
        map.put("token",jwt);
        //将token保存到redis中
        redisCache.setCacheObject("token_"+userId,principal);
        return new ResponseResult(200 , "认证成功",map);
    }

    @Override
    public ResponseResult logout() {
        //从SecurityContextHolder中获取用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserLogin principal = (UserLogin) authentication.getPrincipal();
        Long id = principal.getUser().getId();
        //删除redis中的token
        redisCache.deleteObject("token_"+id);
        return new ResponseResult(200,"退出成功");
    }
}
