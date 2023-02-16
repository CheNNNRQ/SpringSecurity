package com.diodi;

import com.diodi.bean.User;
import com.diodi.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author diodi
 * @create 2023-01-30-17:28
 */
@SpringBootTest
public class MapperTest {
    @Autowired
    private UserMapper userMapper;

    @Resource
    BCryptPasswordEncoder passwordEncoder;
    @Test
    public void testUserMapper(){
        List<User> users = userMapper.selectList(null);
        System.out.println(users);
    }
    @Test
    public void testBCryptPasswordEncoder(){
        //加密
        String encode = passwordEncoder.encode("123");
        System.out.println(encode);
        boolean matches = passwordEncoder.matches("123", encode);
        System.out.println(matches);
    }
}
