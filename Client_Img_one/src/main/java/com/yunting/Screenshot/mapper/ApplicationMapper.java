package com.yunting.Screenshot.mapper;

import com.yunting.Screenshot.entity.Application;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("ApplicationMapper")
public interface ApplicationMapper {
    //查到所有的应用
    List<Application> selectAll();

    //获取该游戏的提现比例
    String getWithdrawPercentage(@Param("gameID") Long gameID);
}