package com.yunting.ws.mapper;


import com.yunting.ws.entity.DayBehaveRecordlist;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component("DayBehaveRecordlistMapper")
public interface DayBehaveRecordlistMapper {

    //    通过playerId获取该玩家每天最新一条 行为记录   (因为是获取到的第一条,所以即使后面误加了行为记录，也因为查到的是第一条，而不会修改错数据)
    DayBehaveRecordlist getDayLastDayBehaveRecordlistByPlayerId(@Param("playerId") Long playerId);


}