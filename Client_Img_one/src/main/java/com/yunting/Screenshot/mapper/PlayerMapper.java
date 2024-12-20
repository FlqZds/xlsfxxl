package com.yunting.Screenshot.mapper;

import com.yunting.Screenshot.entity.Player;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component("PlayerMapper")
public interface PlayerMapper {



    //  根据玩家ID和游戏ID查到玩家微信昵称
    String selectWxNicknameByPlayerId(@Param("playerId") Long playerId, @Param("gameId") String gameId);


}