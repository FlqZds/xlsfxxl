package com.yunting.Screenshot;

import com.yunting.common.utils.JWTutil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
@MapperScan(basePackages = "com.yunting.Screenshot.mapper")
public class FileOneApplication {

    public static void main(String[] args) {

        SpringApplication.run(FileOneApplication.class, args);
    }

}
