package com.yunting.common.configuration;

import com.yunting.common.utils.JWTutil;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class jwTutilConfiguration {
    @Bean("JWTutil")
    public JWTutil jwTutilConfiguration() {
        JWTutil jwTutil = new JWTutil();
        return jwTutil;
    }
}
