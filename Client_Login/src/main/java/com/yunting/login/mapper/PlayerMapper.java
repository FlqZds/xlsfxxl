package com.yunting.login.mapper;

import com.yunting.login.entity.Player;
import com.yunting.login.entity.ScreenshotTask;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component("PlayerMapper")
public interface PlayerMapper {

    //    新增用户
    int insertPlayer(Player player);

    //    更新玩家设备型号id
    int updatePlayerMobileID(@Param("playerId") Long playerId, @Param("mobileID") Long mobileID);

    //    根据微信openId查询玩家信息
    Player selectPlayerByWxOpenId(@Param("wxOpenId") String wxOpenId);

    //  根据玩家ID查到玩家的支付宝相关信息 和玩家此时的余额
    Player selectAliPayInfoByPlayerId(@Param("playerId") Long playerId, @Param("gameId") String gameId);

    //  根据玩家ID和游戏ID查到玩家的所有任务中首次任务
    ScreenshotTask getFirstTaskListByPlayerIdAndGameID(@Param("playerId") Long playerId, @Param("gameId") String gameId);

}