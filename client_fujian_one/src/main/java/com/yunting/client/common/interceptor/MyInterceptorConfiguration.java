package com.yunting.client.common.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class MyInterceptorConfiguration implements WebMvcConfigurer {
    @Resource(name = "ClientInterceptor")
    private ClientInterceptor clientInterceptor;


    @Override
    public void addInterceptors(org.springframework.web.servlet.config.annotation.InterceptorRegistry registry) {

        List <String> list = new ArrayList<>();
        list.add("/swagger-resources/**");
        list.add("/swagger-ui.html/**");
        list.add("/webjars/**");
        list.add("/v2/api-docs/**");

        list.add("/Client/testing");

        list.add("/Client/signOn/");
        list.add("/Client/risking");
        list.add("/Client/addMobile/");
        list.add("/Client/*");
        list.add("/Client/signOff");

        list.add("/Admin/*");
        list.add("/Advertise/reward/callback");

//        MappedInterceptor mappedInterceptor
        registry.addInterceptor(clientInterceptor)
                .excludePathPatterns(list)
                .addPathPatterns("/**");
    }
}
