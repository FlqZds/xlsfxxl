package com.yunting.wlan.mapper;


import com.yunting.common.Dto.ExceptionRecordlsit;
import com.yunting.wlan.entity.AdEncourage;
import com.yunting.wlan.entity.DeviceRecordlist;
import com.yunting.wlan.entity.UserGatheringSetting;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component("DeviceRecordlistMapper")
public interface DeviceRecordlistMapper {

    //    插入一条设备记录
    int insertDeviceRecord(DeviceRecordlist deviceRecordlist);

    //    通过playerIde获取玩家最后一条设备信息
    DeviceRecordlist getLastDeviceRecordByPlayerId(@Param("playerId") Long playerId);

    //    添加激励广告同时 获取 新建的激励广告id
    Integer insertAdEncourage(AdEncourage adEncourage);

    //    插入一条异常记录
    public Integer insertExceptionRecord(ExceptionRecordlsit exceptionRecordlsit);

    //通过游戏id获取聚集设置
    UserGatheringSetting getGatheringSetting(@Param("gameId") String gameId);

}