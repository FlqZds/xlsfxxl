package com.yunting.ws.utils;

import com.yunting.common.Dto.PlayerDTO;
import com.yunting.common.utils.RedisUtil_session;
import com.yunting.common.utils.RedisUtils_Wlan;
import com.yunting.common.utils.ST;

import com.yunting.ws.config.websocket.MyConfigurator;
import com.yunting.ws.dto.PlayerMetaData;
import com.yunting.ws.entity.DayBehaveRecordlist;
import com.yunting.ws.entity.Player;
import com.yunting.ws.entity.ScreenshotTask;
import com.yunting.ws.mapper.DayBehaveRecordlistMapper;
import com.yunting.ws.mapper.PlayerMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.Session;
import java.math.BigDecimal;
import java.time.LocalDate;

@Component("GatheringUtils")
@Slf4j
public class sessionUtils {
    @Resource(name = "RedisUtils")
    private RedisUtils_Wlan rs;

    @Resource(name = "RedisUtil_session")
    private RedisUtil_session rus;

    @Resource(name = "PlayerMapper")
    private PlayerMapper playerMapper;


    @Resource(name = "DayBehaveRecordlistMapper")
    private DayBehaveRecordlistMapper dayBehaveMapper;

    @Resource(name = "ST")
    private ST st;

    /***
     * 在线用户退出
     * 清除
     * 1.该玩家自己的wifi列表
     * 2.该玩家的playerID对应序号 才序号列表中抹除
     * 3.从用户id列表中移除该用户
     *
     * 4.该在线用户的一些常用信息
     * @param playerId
     * @param location
     */
    public void userExit(String playerId, String location) {
        for (String s_mac : rs.setMembers(playerId)) { //从用户id列表中移除该用户
            if (rs.setMembers(location + s_mac).size() == 1) {
                rs.sAdd(location + s_mac, "");
            }
            rs.sRemove(location + s_mac, playerId);
        }
        rs.delete(playerId);//清除该用户wifi列表
        String key = rs.hGet(location + "AL", playerId) + "";
        if (key != null) {
            rs.hDelete(location, key);
        }
        rs.hDelete(location + "AL", playerId); //将该玩家从该位置 - 移除

        rus.delete(playerId);//该在线用户的一些常用信息
        rus.delete(playerId + "State"); //标识该在线用户的状态 - 移除
        rus.sRemove("WEBSOCKET", playerId);  //从在线用户列表 - 移除
    }


    /***
     * 该session是哪个在线玩家的
     * @param session
     * @return 返回该session的playerID
     */
    public String thisSessionBelongsTo(Session session) {
        String playerId = session.getUserProperties().get(MyConfigurator.PLayerID).toString();
        return playerId;
    }


    /***
     * 将在线用户的一些常用信息放入redis中，并返回playerID
     * @param session 该用户的session
     * @return 该玩家的playerID
     */
    public String makeSessionFlag(Session session) {
        String playerID = thisSessionBelongsTo(session);

        Player player = playerMapper.selectPlayerByPlayerId(playerID);
        String location = playerMapper.getLastLocationByPlayerId(playerID, LocalDate.now());

        rus.hPut(playerID, "wxName", player.getWxNickname()); //玩家的昵称
        rus.hPut(playerID, "gameID", st.GameId());//游戏id
        rus.hPut(playerID, "packageName", st.PackageName());//包名
        rus.hPut(playerID, "pos", location);//位置信息

        return playerID;
    }


    /***
     * 通过包名获取游戏设置
     * @return
     * 1玩家红包余额
     * <p>
     * 2总累计红包金额
     * <p>
     * 3今日累计红包金额
     * <p>
     * 4此时的提现比例
     * <p>
     * 5玩家支付宝id
     * <p>
     * 6玩家姓名
     * <p>
     * 7玩家任务列表
     */
    public PlayerMetaData getGameSetting(String playerID) {
        Long playerId = Long.parseLong(playerID);
        PlayerMetaData playerMetaData = new PlayerMetaData();

        ScreenshotTask task = playerMapper.getFirstTaskListByPlayerIdAndGameID(playerId, st.GameId());
        Player player = playerMapper.selectAliPayInfoByPlayerId(playerId, st.GameId());
        DayBehaveRecordlist player_day_record = dayBehaveMapper.getDayLastDayBehaveRecordlistByPlayerId(playerId); //该玩家当日的留存数据

        if (player_day_record == null) {
            player_day_record.setTotalred(BigDecimal.ZERO);
            player_day_record.setTodayred(BigDecimal.ZERO);
        }
        BigDecimal totalred = player_day_record.getTotalred(); //玩家总累计红包金额
        BigDecimal todayred = player_day_record.getTodayred(); //玩家当日红包余额
        BigDecimal inRed = player.getInRed();                  //玩家余额
        String payLoginId = player.getPayLoginId(); //支付宝id
        String realName = player.getRealName(); //姓名
//设置
        //截图设置
        playerMetaData.setScreenshotSettingVal(st.Codebit_Max_val());
        playerMetaData.setTransLimitDaily(st.Daily_Max_Submit_Num());
        playerMetaData.setTransRewardCont(st.Daily_Max_Watch_Num());
        playerMetaData.setMaxRestTime(st.Reset_Max_Time());
        String shotOptions = st.isShot_Switch() ? "1" : "0";
        playerMetaData.setScreenshotSettingOptions(shotOptions);

        playerMetaData.setNoticeMSG(st.Notification());
        playerMetaData.setAdvWatchInterval(st.ADV_Interval());
        playerMetaData.setWithdrawPercentage(st.Withdraw_Percentage());//提现比例
//玩家数据
        playerMetaData.setInRed(String.valueOf(inRed));
        playerMetaData.setTotalRed(String.valueOf(totalred));
        playerMetaData.setTodayRed(String.valueOf(todayred));
//pay
        playerMetaData.setPayLoginId(payLoginId);
        playerMetaData.setRealName(realName);

//任务
        if (task == null || task.getBonus() == null) {
            playerMetaData.setBonus(BigDecimal.ZERO);
            playerMetaData.setTaskProcess(0);
            log.info("玩家:|-" + playerId + "-|,任务金不存在,现已读取游戏设置");
            return playerMetaData;
        }
        playerMetaData.setBonus(task.getBonus());
        playerMetaData.setTaskProcess(task.getTaskProcess());
        log.info("玩家:|-" + playerId + "-|已读取游戏设置");
        return playerMetaData;
    }

}
