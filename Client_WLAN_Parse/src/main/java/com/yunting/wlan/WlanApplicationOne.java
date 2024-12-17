package com.yunting.wlan;

import com.dtflys.forest.springboot.annotation.ForestScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableWebSocket
@EnableScheduling
@EnableSwagger2

@MapperScan(basePackages = "com.yunting.client.mapper")

@ForestScan(basePackages = "com.yunting.forest")
@SpringBootApplication
public class WlanApplicationOne {

    public static void main(String[] args) {

        SpringApplication.run(WlanApplicationOne.class, args);
    }
}
