package com.yunting.sum;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling

@MapperScan(basePackages = "com.yunting.sum.mapper")
@SpringBootApplication
public class SumOneApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(SumOneApplication.class, args);
    }
}
