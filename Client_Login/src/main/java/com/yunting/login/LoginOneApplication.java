package com.yunting.login;

import com.dtflys.forest.springboot.annotation.ForestScan;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@ForestScan(basePackages = "com.yunting.login.forest")
@MapperScan(basePackages = "com.yunting.login.mapper")
@SpringBootApplication
public class LoginOneApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(LoginOneApplication.class, args);
    }
}
