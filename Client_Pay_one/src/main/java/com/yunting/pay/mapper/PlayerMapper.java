package com.yunting.pay.mapper;

import com.yunting.pay.entity.Player;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component("PlayerMapper")
public interface PlayerMapper {
    //    更新玩家的余额
    int updatePlayerInRed(@Param("playerId") Long playerId, @Param("inred") BigDecimal inred);


    //    更新玩家的支付宝认证信息
    int refreshPlayerRealInfo(@Param("playerId") Long playerId, @Param("payLoginId") String payLoginId, @Param("realName") String realName);

    //  根据玩家ID查到玩家
    Player selectPlayerByPlayerId(@Param("playerId") Long playerId);

    //  根据支付宝ID查到玩家
    Long selectPlayerByPayID(@Param("PayID") String PayID);

    //  根据支付宝ID查到玩家
    Long selectPlayerByRealName(@Param("RealName") String RealName);

    //  根据玩家ID和游戏ID查到玩家微信昵称
    String selectWxNicknameByPlayerId(@Param("playerId") Long playerId, @Param("gameId") Long gameId);

    //  根据玩家ID和游戏ID查到玩家的余额
    BigDecimal selectInRedByPlayerId(@Param("playerId") Long playerId, @Param("gameId") Long gameId);

}