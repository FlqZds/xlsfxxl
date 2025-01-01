package com.yunting.common.configuration;

import com.yunting.common.utils.RedisUtil_Record;
import com.yunting.common.utils.RedisUtil_session;
import com.yunting.common.utils.RedisUtils_Wlan;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class rurConfiguration {
    @Bean("RedisUtil_Record")
    public RedisUtil_Record redisUtil_Record() {
        return new RedisUtil_Record();
    }

    @Bean("RedisUtils")
    public RedisUtils_Wlan redisUtil_wlan() {
        return new RedisUtils_Wlan();
    }

    @Bean("RedisUtil_session")
    public RedisUtil_session redisUtil_session() {
        return new RedisUtil_session();
    }
}
