package com.yunting.wlan.mapper;

import com.yunting.wlan.entity.Location;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component("LocationMapper")
public interface LocationMapper {

//    插入相应定位信息数据
    int insertLocation(Location location);

//    通过玩家ID查到该玩家最近一次的最后一条的位置数据
    String getLocationByPlayerId(String playerId);

//    通过玩家ID查到该玩家最后一条位置信息
    Location getLastLocationByPlayerId(@Param("playerId") String playerId,@Param("today") LocalDate today);



}