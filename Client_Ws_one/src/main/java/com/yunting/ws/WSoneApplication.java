package com.yunting.ws;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;


@EnableWebSocket
@MapperScan(basePackages = "com.yunting.ws.mapper")
@SpringBootApplication
public class WSoneApplication {
    public static void main(String[] args) {

        SpringApplication.run(WSoneApplication.class, args);
    }
}
