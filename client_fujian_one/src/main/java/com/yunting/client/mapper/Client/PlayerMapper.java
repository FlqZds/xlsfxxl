package com.yunting.client.mapper.Client;

import com.yunting.client.DTO.RetainActive;
import com.yunting.client.entity.Player;
import com.yunting.client.entity.ScreenshotTask;
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

    //    更新玩家的余额
    int updatePlayerInRed(@Param("playerId") Long playerId, @Param("inred") BigDecimal inred);

    //    更改玩家权限 (设置特殊用户 解除特殊用户)
    int changePlayerPower(@Param("playerId") String playerId, @Param("power") Character power, @Param("gameId") String gameId);

    //    更改玩家状态 (封禁 解封 )
    int changePlayerStatus(@Param("playerId") String playerId, @Param("status") Character status, @Param("gameId") String gameId);

    //    更新玩家的支付宝认证信息
    int refreshPlayerRealInfo(@Param("playerId") Long playerId, @Param("payLoginId") String payLoginId, @Param("realName") String realName);

    //    查到玩家权限和状态
    Player selectPlayerPowerAndStatus(@Param("playerId") String playerId, @Param("gameId") String gameId);

    //    找到所有的 该种gameid 的玩家
    List<Long> selectPlayersByGameId(@Param("gameId") Long gameId);

    //    找到所有的 该种gameid 的玩家
    List<Player> selectPlayersInQueryCondition(@Param("active") RetainActive active);

    //    根据微信openId查询玩家信息
    Player selectPlayerByWxOpenId(@Param("wxOpenId") String wxOpenId);

    //    根据微信openId查询Status
    Character selectPlayerStatusByWxOpenId(@Param("wxOpenId") String wxOpenId);

    //    根据玩家ID查询Status
    Character selectPlayerStatusByPlayerId(@Param("playerId") String playerId);

    //  根据玩家ID查到玩家的设备ID
    Long selectMobileIDByPlayerId(@Param("playerId") String playerId);

    //  根据玩家ID查到该玩家的设备名称
    String selectMobileNameByPlayerId(@Param("playerId") String playerId);

    //  根据玩家ID查到玩家
    Player selectPlayerByPlayerId(@Param("playerId") Long playerId);

    //  根据玩家ID查到玩家的支付宝相关信息 和玩家此时的余额
    Player selectAliPayInfoByPlayerId(@Param("playerId") Long playerId, @Param("gameId")Long gameId);

    //  根据支付宝ID查到玩家
    Long selectPlayerByPayID(@Param("PayID") String PayID);

    //  根据支付宝ID查到玩家
    Long selectPlayerByRealName(@Param("RealName") String RealName);

    //  根据玩家ID和游戏ID查到玩家微信昵称
    String selectWxNicknameByPlayerId(@Param("playerId") Long playerId, @Param("gameId") Long gameId);

    //  根据玩家ID和游戏ID查到玩家的余额
    BigDecimal selectInRedByPlayerId(@Param("playerId") Long playerId, @Param("gameId") Long gameId);


    //  根据玩家ID和游戏ID查到玩家的所有任务中首次任务
    ScreenshotTask getFirstTaskListByPlayerIdAndGameID(@Param("playerId") Long playerId, @Param("gameId") Long gameId);

}