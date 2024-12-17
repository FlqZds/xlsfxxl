package com.yunting.client.mapper;

import com.alibaba.fastjson2.JSONObject;
import com.yunting.client.DTO.RetainActive;
import com.yunting.client.entity.MobileDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("MobileDetailMapper")
public interface MobileDetailMapper {

//    插入一条设备信息数据
    int insert(MobileDetail record);

//    查到所有的设备信息数据
    List<MobileDetail> selectAllMobileByCondition(@Param("active")RetainActive active);

    //    根据主键查到该玩家最新的设备信息数据
    public MobileDetail selectByPrimaryKey(Long mobileId);

    // 根据id修改设备信息
    public Integer changeMobileByID(@Param("mobileId") Long mobileId, @Param("deviceName") String deviceName);

//  根据手机品牌和手机型号,CPU,频率 查到相关的设备id
    public MobileDetail selectMobileNameAndBrand(@Param("cpu") String cpu, @Param("cpuFluency") String cpuFluency,@Param("deviceModel")String deviceModel,@Param("deviceType")String deviceType);

    // 根据id修改设备信息
    public Integer changeMobileSystem(@Param("mobileId") Long mobileId, @Param("mobileSystem") String mobileSystem);

    //     拿到所有不允许通过的设备品牌
    List<String> getAllImapprovalBrand();
}