package com.yunting.client.common.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.yunting.client.common.config.websocket.MyConfigurator;
import com.yunting.client.entity.Application;
import com.yunting.client.entity.Player;
import com.yunting.client.mapper.Client.LocationMapper;
import com.yunting.client.mapper.Client.PlayerMapper;
import com.yunting.common.results.ResultMessage;
import com.yunting.forest.ForestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.Session;
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

    @Resource(name = "LocationMapper")
    private LocationMapper locationMapper;

    @Resource(name = "ForestService")
    private ForestService forest;

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

        Player player = playerMapper.selectPlayerByPlayerId(Long.valueOf(playerID));
        String location = locationMapper.getLastLocationByPlayerId(playerID, LocalDate.now()).getLocation();
        String application_json = forest.getApplication(player.getGameId().toString());
        ResultMessage resultMessage = JSONObject.parseObject(application_json, ResultMessage.class);
        Application application = JSON.to(Application.class, resultMessage.getData());

        rus.hPut(playerID, "wxName", player.getWxNickname()); //玩家的昵称
        rus.hPut(playerID, "gameID", player.getGameId() + "");//游戏id
        rus.hPut(playerID, "packageName", application.getPackageName());//包名
        rus.hPut(playerID, "pos", location);//位置信息

        return playerID;
    }
}
