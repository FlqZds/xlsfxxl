package com.yunting.wlan.mapper;

import com.yunting.wlan.entity.Player;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component("PlayerMapper")
public interface PlayerMapper {

    //  根据玩家ID查到玩家的设备ID
    Long selectMobileIDByPlayerId(@Param("playerId") String playerId);

    //  根据玩家ID查到该玩家的设备名称
    String selectMobileNameByPlayerId(@Param("playerId") String playerId);

    //  根据玩家ID查到玩家
    Player selectPlayerByPlayerId(@Param("playerId") Long playerId);

    //    查到玩家权限和状态
    Player selectPlayerPowerAndStatus(@Param("playerId") String playerId, @Param("gameId") String gameId);

    //    更新玩家设备型号id
    int updatePlayerMobileID(@Param("playerId") Long playerId, @Param("mobileID") Long mobileID);
}