package com.yunting.client.mapper;

import com.yunting.client.entity.Application;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("ApplicationMapper")
public interface ApplicationMapper {
    //查到所有的应用
    List<Application> selectAll();
}