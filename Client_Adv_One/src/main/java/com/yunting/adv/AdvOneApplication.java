package com.yunting.adv;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@MapperScan(basePackages = "com.yunting.adv.mapper")
@SpringBootApplication
public class AdvOneApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(AdvOneApplication.class, args);
    }
}

