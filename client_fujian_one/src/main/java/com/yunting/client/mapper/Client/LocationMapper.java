package com.yunting.client.mapper.Client;

import com.yunting.client.entity.Location;
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

//    更新玩家位置信息
    int updateLocation(@Param("position")String position, @Param("recordTime") LocalDateTime recordTime, @Param("playerId")String playerId);

//    通过玩家ID 查到该玩家的所有位置信息
    List<Location> getLocationInfoByPlayerID(@Param("playerId") Long playerId,@Param("recordTime") LocalDate localDate);



}