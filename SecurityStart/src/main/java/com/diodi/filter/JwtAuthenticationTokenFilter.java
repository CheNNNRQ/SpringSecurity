package com.diodi.filter;

import com.diodi.bean.UserLogin;
import com.diodi.utils.JwtUtil;
import com.diodi.utils.RedisCache;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @author diodi
 * @create 2023-02-16-16:34
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    //继承OncePerRequestFilter，保证过滤器只被调用一次

    @Resource
    RedisCache redisCache;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取请求头中的token
        String token = request.getHeader("token");
        //判断token是否为空 如果为空就放行
        if (token == null) {
            filterChain.doFilter(request, response);
            //---->
            //<---- 还会回来调用 所以直接return
            return;
        }
        //解析token
        String userId;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            //subject就是创建jwt时候传入的userId
            userId = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token不合法");
        }
        //使用userid从redis中得到用户信息
        UserLogin userLogin = redisCache.getCacheObject("token_" + userId);
        //判断用户信息是否为空
        if (Objects.isNull(userLogin)) {
            throw new RuntimeException("用户未登录");
        }
        //将用户信息放入SecurityContextHolder中
        //TODO 获取权限信息封装到Authentication中
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userLogin, null, userLogin.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //放行
        filterChain.doFilter(request, response);
    }
}
