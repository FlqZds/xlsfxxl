package com.yunting.common.configuration;

import com.yunting.common.utils.ST;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class StConfiguration {
    @Bean("ST")
    public ST st() {
        return new ST();
    }
}
