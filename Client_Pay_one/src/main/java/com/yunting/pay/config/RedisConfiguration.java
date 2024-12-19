package com.yunting.pay.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@SpringBootConfiguration
public class RedisConfiguration extends RedisAutoConfiguration {
    @Value("${host}")
    private String wifiHost;

    @Value("${instance_wifi_port}")
    private Integer wifiPort;

//    @Value("${spring.redis.instance_wifi.password}")
//    private String password;


    @Value("${host}")
    private String sessionHost;

    @Value("${instance_websocket_port}")
    private Integer sessionPort;

//    @Value("${spring.redis.instance_websocket.password}")
//    private String secondPassword;

    @Value("${host}")
    private String recordHost;

    @Value("${instance_Record_port}")
    private Integer recordPort;


    //wifi信息的
    @Bean(name = "wlanConnectionFactory")
    @Primary  //默认选择这个数据源进行执行
    @Qualifier("wlanConnectionFactory")
    public LettuceConnectionFactory wlanConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(wifiHost);
        config.setPort(wifiPort);
//        config.setPassword(RedisPassword.of(password));
        return new LettuceConnectionFactory(config);
    }

    //session信息的
    @Bean(name = "sessionInfoFactory")
    public LettuceConnectionFactory sessionInfoRedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(sessionHost);
        config.setPort(sessionPort);
//        config.setPassword(RedisPassword.of(secondPassword));
        return new LettuceConnectionFactory(config);
    }

    //record(广告记录+用户留存信息的)
    @Bean(name = "recordInfoFactory")
    public LettuceConnectionFactory recordInfoRedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(recordHost);
        config.setPort(recordPort);
//        config.setPassword(RedisPassword.of(secondPassword));
        return new LettuceConnectionFactory(config);
    }



}
