package com.diodi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.diodi.mapper")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run( Application.class,args);
        System.out.println("******************************服务启动成功******************************");
    }
}
