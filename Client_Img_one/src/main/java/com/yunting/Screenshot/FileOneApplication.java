package com.yunting.Screenshot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.yunting.Screenshot.mapper")
public class FileOneApplication {
    public static void main(String[] args) {

        SpringApplication.run(FileOneApplication.class, args);
    }

}
