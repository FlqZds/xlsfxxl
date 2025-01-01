package com.yunting.ws.config.websocket;

import com.yunting.common.Dto.PlayerDTO;
import com.yunting.common.exception.AppException;
import com.yunting.common.results.ResponseEnum;
import com.yunting.common.utils.JWTutil;
import com.yunting.common.utils.RedisUtil_Record;
import com.yunting.common.utils.ST;
import com.yunting.common.utils.SpringRollBackUtil;
import com.yunting.ws.SpringBeanContext;
import com.yunting.ws.entity.DayBehaveRecordlist;
import com.yunting.ws.entity.Player;
import com.yunting.ws.mapper.PlayerMapper;
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
        String playerId = map.get("playerId").toString();

        recordingPlayerBehaver(playerId);  //行为记录

        userProperties.put(PLayerID, playerId);
    }

    //   更新 位置信息 + 用户每日行为的记录
    @Transactional(rollbackFor = Exception.class)
    public void recordingPlayerBehaver(String playerid) {
        PlayerMapper playerMapper = SpringBeanContext.getContext().getBean(PlayerMapper.class);
        RedisUtil_Record rur = SpringBeanContext.getContext().getBean("RedisUtil_Record", RedisUtil_Record.class);
        ST st = SpringBeanContext.getContext().getBean(ST.class);

        Player player = playerMapper.selectPlayerByPlayerId(playerid);

        if (rur.getBit("retain_bitMap", Long.parseLong(playerid)) == false) {
            String way = "1";
            if (st.Retain_Way()) {
                way = "0";
            }
            if (!st.Retain_Way()) {
                way = "1";
            }


            //        留存记录插入
            DayBehaveRecordlist dayBehave = DayBehaveRecordlist.builder()
                    .playerId(playerid).appId(player.getGameId().toString())
                    .retainTime(LocalDateTime.now()).retainWay(way)
                    .todayred(BigDecimal.ZERO).totalred(BigDecimal.ZERO)
                    .todayEncourageAdvCount(0)
                    .serviceCallBackAdvCount(0)
                    .serviceCallBackRewardCount(0)
                    .build();

            try {
                playerMapper.addDayBehaveRecordlist(dayBehave);

                rur.setBit("retain_bitMap", Long.parseLong(playerid), true);

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
