package com.yunting.pay;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@MapperScan(basePackages = "com.yunting.pay.mapper")
@SpringBootApplication
public class PayOneApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(PayOneApplication.class, args);
    }
}

