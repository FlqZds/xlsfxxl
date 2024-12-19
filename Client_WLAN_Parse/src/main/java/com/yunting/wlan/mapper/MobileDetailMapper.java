package com.yunting.wlan.mapper;

import com.yunting.wlan.entity.MobileDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("MobileDetailMapper")
public interface MobileDetailMapper {

//    插入一条设备信息数据
    int insert(MobileDetail record);

    //    根据主键查到该玩家最新的设备信息数据
    public MobileDetail selectByPrimaryKey(Long mobileId);

    // 根据id修改设备信息
    public Integer changeMobileByID(@Param("mobileId") Long mobileId, @Param("deviceName") String deviceName);

//  根据手机品牌和手机型号,CPU,频率 查到相关的设备id
    public MobileDetail selectMobileNameAndBrand(@Param("cpu") String cpu, @Param("cpuFluency") String cpuFluency,@Param("deviceModel")String deviceModel,@Param("deviceType")String deviceType);

    // 根据id修改设备信息
    public Integer changeMobileSystem(@Param("mobileId") Long mobileId, @Param("mobileSystem") String mobileSystem);

}