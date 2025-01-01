package com.yunting.ws.mapper;

import com.yunting.ws.entity.DayBehaveRecordlist;
import com.yunting.ws.entity.Player;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component("PlayerMapper")
public interface PlayerMapper {
    //    根据玩家ID查询Status
    Character selectPlayerStatusByPlayerId(@Param("playerId") String playerId);

    //  根据玩家ID查到玩家
    Player selectPlayerByPlayerId(@Param("playerId") String playerId);

    //  根据玩家ID和游戏ID查到玩家的余额
    BigDecimal selectInRedByPlayerId(@Param("playerId") Long playerId, @Param("gameId") Long gameId);

    //    记录一条每日行为记录
    Integer addDayBehaveRecordlist(@Param("dayRecord") DayBehaveRecordlist dayBehaveRecordlist);

    //    通过玩家ID查到该玩家最后一条位置信息
    String getLastLocationByPlayerId(@Param("playerId") String playerId,@Param("today") LocalDate today);


}