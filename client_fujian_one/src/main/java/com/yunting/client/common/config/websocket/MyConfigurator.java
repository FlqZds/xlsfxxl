package com.yunting.client.common.config.websocket;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.yunting.client.common.utils.*;
import com.yunting.SpringBeanContext;
import com.yunting.client.entity.DayBehaveRecordlist;
import com.yunting.client.entity.Player;
import com.yunting.client.entity.setting.GameSetting;
import com.yunting.client.mapper.Client.PlayerMapper;
import com.yunting.client.mapper.DayBehaveRecordlistMapper;
import com.yunting.clientservice.service.RecordService;
import com.yunting.common.Dto.PlayerDTO;
import com.yunting.common.exception.AppException;
import com.yunting.common.results.ResponseEnum;
import com.yunting.common.results.ResultMessage;
import com.yunting.common.utils.JWTutil;
import com.yunting.common.utils.RedisUtil_Record;
import com.yunting.common.utils.RedisUtil_session;
import com.yunting.common.utils.SpringRollBackUtil;
import com.yunting.forest.ForestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootConfiguration
@Slf4j
public class MyConfigurator extends ServerEndpointConfig.Configurator {

    /***
     * 判断token是否合法
     * 同时将playerID和gameID封装到map中
     * @param authorization
     */
    @Transactional(rollbackFor = Exception.class)
    public Map judgeToken(String authorization) {
        JWTutil jwTutil = SpringBeanContext.getContext().getBean(JWTutil.class);
        PlayerMapper playerMapper = SpringBeanContext.getContext().getBean(PlayerMapper.class);

        HashMap<String, String> play_map = new HashMap<>();
        String token = authorization.substring(1, authorization.length() - 1);
        PlayerDTO playerDTO = jwTutil.checkJwt_master(token);

        String gameId = playerDTO.getGameId() + "";
        String playerId = playerDTO.getPlayerId() + "";

        Character status = playerMapper.selectPlayerStatusByPlayerId(playerId);
        if (status.equals('0')) {
            log.error("玩家:" + playerId + " 已被封号");
            throw new AppException(ResponseEnum.BAN_USER_OUT);
        }

        play_map.put("gameId", gameId);
        play_map.put("playerId", playerId);
        return play_map;
    }

    public static String Authorization = "Authorization";//token
    public static String PLayerID = "Player.ID";

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {

        RedisUtil_session rus = SpringBeanContext.getContext().getBean("RedisUtil_session", RedisUtil_session.class);

        RecordService recordService = SpringBeanContext.getContext().getBean(RecordService.class);

        Map<String, Object> userProperties = sec.getUserProperties();

        //获取请求头
        Map<String, List<String>> headers = request.getHeaders();

        if (headers.isEmpty() == true) {
            log.error("请求头为空,解析失败...");
            throw new AppException(ResponseEnum.DATA_NOT_FOUND);
        }

        if (headers.containsKey(Authorization) == false) {
            log.error("请求头格式不正确，判定为非法请求...");
            throw new AppException(ResponseEnum.MISSING_REQUEST_PARAMETER_EXCEPTION);
        }

//        拿到请求头中携带过来的东西
        String authorization = headers.get(Authorization).toString();

        Map map = judgeToken(authorization); //判断token
        String gameId = map.get("gameId").toString();
        String playerId = map.get("playerId").toString();

        recordingPlayerBehaver(playerId, gameId);  //行为记录

//        if (rus.setMembers("DET").contains(playerId) == false) {
//            log.info("未参与聚集校验,请先去聚集验证,玩家:" + playerId, new AppException(ResponseEnum.PLAYER_NOT_PARTICIPATE_IN_GATHERING));
//            throw new AppException(ResponseEnum.PLAYER_NOT_PARTICIPATE_IN_GATHERING);
//        }
        userProperties.put(PLayerID, playerId);
    }

    //   更新 位置信息 + 用户每日行为的记录
    @Transactional(rollbackFor = Exception.class)
    public void recordingPlayerBehaver(String playerid, String gameID) {
        PlayerMapper playerMapper = SpringBeanContext.getContext().getBean(PlayerMapper.class);
        DayBehaveRecordlistMapper dayBehaveRecordMapper = SpringBeanContext.getContext().getBean(DayBehaveRecordlistMapper.class);
        ForestService forestService = SpringBeanContext.getContext().getBean(ForestService.class);
        RedisUtil_Record rur = SpringBeanContext.getContext().getBean(RedisUtil_Record.class);

        Long playerID = Long.parseLong(playerid);
        Player player = playerMapper.selectPlayerByPlayerId(playerID);
        String gameSetting_json = forestService.getGameSetting(gameID);
        ResultMessage resultMessage = JSONObject.parseObject(gameSetting_json, ResultMessage.class);
        GameSetting gameSetting = JSON.to(GameSetting.class, resultMessage.getData());
        String retainWay = gameSetting.getRetainWay();  //留存方式


        if (rur.getBit("retain_bitMap", playerID) == false) {

            //        留存记录插入
            DayBehaveRecordlist dayBehave = DayBehaveRecordlist.builder()
                    .playerId(playerid).appId(player.getGameId().toString())
                    .retainTime(LocalDateTime.now()).retainWay(retainWay)
                    .todayred(BigDecimal.ZERO).totalred(BigDecimal.ZERO)
                    .todayEncourageAdvCount(0)
                    .serviceCallBackAdvCount(0)
                    .serviceCallBackRewardCount(0)
                    .build();

            try {
                dayBehaveRecordMapper.addDayBehaveRecordlist(dayBehave);

                rur.setBit("retain_bitMap", playerID, true);

                log.info("用户:" + playerid + " 当日行为记录已留存");

            } catch (Exception e) {
                SpringRollBackUtil.rollBack();
                log.error("用户:" + playerid + " 当日行为记录留存失败");
                throw new AppException(ResponseEnum.RECORD_BEHAVE_ADD_FAILED);
            }

        }

    }


    //初始化session链接对象
    @Override
    public <T> T getEndpointInstance(Class<T> clazz) throws InstantiationException {

        return super.getEndpointInstance(clazz);
    }
}
