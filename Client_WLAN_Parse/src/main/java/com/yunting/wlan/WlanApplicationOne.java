package com.yunting.wlan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@MapperScan(basePackages = "com.yunting.wlan.mapper")
@SpringBootApplication
public class WlanApplicationOne {

    public static void main(String[] args) {

        SpringApplication.run(WlanApplicationOne.class, args);
    }
}
