package com.yunting.client.mapper.Client;

import com.yunting.client.entity.DeviceRecordlist;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component("DeviceRecordlistMapper")
public interface DeviceRecordlistMapper {

//    插入一条设备记录
    int insertDeviceRecord(DeviceRecordlist deviceRecordlist);

//    通过playerId获取玩家所有的设备信息
    DeviceRecordlist getDeviceRecordByPlayerId(Long playerId);

//    通过playerIde获取玩家最后一条设备信息
    DeviceRecordlist getLastDeviceRecordByPlayerId(@Param("playerId") Long playerId);

}