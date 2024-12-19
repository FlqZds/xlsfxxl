package com.yunting.adv.mapper;

import com.yunting.adv.entity.Player;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component("PlayerMapper")
public interface PlayerMapper {
    //    更新玩家的余额
    int updatePlayerInRed(@Param("playerId") Long playerId, @Param("inred") BigDecimal inred);


    //  根据玩家ID查到玩家
    Player selectPlayerByPlayerId(@Param("playerId") Long playerId);


}