package com.yunting.common.interceptor;

import com.yunting.common.utils.JWTutil;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootConfiguration
public class MyInterceptorAutoConfiguration implements WebMvcConfigurer {

    @Bean("ClientInterceptor")
    public ClientInterceptor playerInterceptor() {
        return new ClientInterceptor();
    }


    @Override
    public void addInterceptors(org.springframework.web.servlet.config.annotation.InterceptorRegistry registry) {

        List<String> list = new ArrayList<>();
        list.add("/swagger-resources/**");
        list.add("/swagger-ui.html/**");
        list.add("/webjars/**");
        list.add("/v2/api-docs/**");

        list.add("/sum/upd");
        list.add("/sum/addMobile/");

        list.add("/net/operateUser");

        list.add("/Client/collection");
        list.add("/Client/signOn");
        list.add("/Client/risking");

        list.add("/Admin/*");
        list.add("/Advertise/reward/callback");

//        MappedInterceptor mappedInterceptor
        registry.addInterceptor(new ClientInterceptor())
                .excludePathPatterns(list)
                .addPathPatterns("/**");
    }
}
