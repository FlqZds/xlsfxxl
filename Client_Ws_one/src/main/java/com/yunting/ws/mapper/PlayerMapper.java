package com.yunting.ws.mapper;

import com.yunting.ws.entity.DayBehaveRecordlist;
import com.yunting.ws.entity.OperationRecordlist;
import com.yunting.ws.entity.Player;
import com.yunting.ws.entity.ScreenshotTask;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    //    更新一条该用户的当日行为记录
    Integer updatePlayerDayrecord(@Param("playerId") String playerId, @Param("retainTime") LocalDateTime retainTime, @Param("date") LocalDate date);


    //    通过玩家ID查到该玩家最后一条位置信息
    String getLastLocationByPlayerId(@Param("playerId") String playerId, @Param("today") LocalDate today);


    //    更改玩家权限 (设置特殊用户 解除特殊用户)
    int changePlayerPower(@Param("playerId") String playerId, @Param("power") Character power, @Param("gameId") String gameId);

    //    更改玩家状态 (封禁 解封 )
    int changePlayerStatus(@Param("playerId") String playerId, @Param("status") Character status, @Param("gameId") String gameId);

    //    插入一条修改记录
    int insert(OperationRecordlist record);


    //  根据玩家ID查到玩家的支付宝相关信息 和玩家此时的余额
    Player selectAliPayInfoByPlayerId(@Param("playerId") Long playerId, @Param("gameId") String gameId);

    //  根据玩家ID和游戏ID查到玩家的所有任务中首次任务
    ScreenshotTask getFirstTaskListByPlayerIdAndGameID(@Param("playerId") Long playerId, @Param("gameId") String gameId);


}