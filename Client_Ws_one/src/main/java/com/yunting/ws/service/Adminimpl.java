package com.yunting.ws.service;


import com.alibaba.fastjson2.JSON;
import com.google.gson.Gson;
import com.yunting.common.exception.AppException;
import com.yunting.common.results.ResponseEnum;
import com.yunting.common.results.ResultMessage;
import com.yunting.common.utils.RedisUtil_session;
import com.yunting.common.utils.ST;
import com.yunting.common.utils.SpringRollBackUtil;
import com.yunting.ws.entity.OperationRecordlist;
import com.yunting.ws.entity.Player;
import com.yunting.ws.entity.incondition;
import com.yunting.ws.mapper.PlayerMapper;
import com.yunting.ws.utils.sessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.websocket.Session;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.yunting.ws.Ws.Wso.SESSION_POOL;


@Slf4j
@Service("AdminService")
public class Adminimpl implements AdminService {

    @Resource(name = "PlayerMapper")
    private PlayerMapper playerMapper;

    @Resource(name = "ST")
    private ST st;

    @Resource(name = "RedisUtil_session")
    private RedisUtil_session rus;

    @Resource(name = "GatheringUtils")
    private com.yunting.ws.utils.sessionUtils sessionUtils;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultMessage changeUserPower(incondition incondition) throws IOException {

        String condition_json = incondition.getCondition();

        incondition condition = JSON.to(incondition.class, condition_json);

        Long playerId = Long.parseLong(condition.getPlayerId());
        Player player = playerMapper.selectPlayerByPlayerId(playerId + "");

        Character status = player.getStatus();
        String power = player.getSpecial();

        OperationRecordlist operationRecord = OperationRecordlist.builder()
                .appid(Long.parseLong(st.GameId()))
                .operationReason(condition.getOperationMsg()).operationTime(LocalDateTime.now())
                .wxOpenId(player.getWxOpenId()).playerId(player.getPlayerId()).wxNickname(player.getWxNickname())
                .build();
        try {
            switch (condition.getCondition()) {
                case "ban": {  //封号
                    if (status == 0) {
                        log.error("用户已封禁，无需再次封禁");
                        return new ResultMessage(ResponseEnum.ALEREADY_BANNED, null);
                    }
                    playerMapper.changePlayerStatus(condition.getPlayerId(), '0', condition.getAppid());
                    operationRecord.setOperationType("封禁");
                    playerMapper.insert(operationRecord);

                    if (rus.get(playerId + "State") != null) { //玩家在线,就能拿到值
                        String location = rus.hGet(playerId + "", "pos").toString();

                        Session session = SESSION_POOL.get(playerId + "");
                        Gson gson = new Gson();
                        session.getAsyncRemote().sendText(gson.toJson(ResponseEnum.BAN_USER_OUT));
                        session.close();
                        SESSION_POOL.remove(playerId + "");
                        sessionUtils.userExit(playerId + "", location); //移除玩家的redis在线状态
                    }
                    log.info("用户操作已完成,封号");
                    break;
                }
                case "unban": {  //解封
                    if (status == 1) {
                        log.error("用户已解封，无需再次解封");
                        throw new AppException(ResponseEnum.ALEREADY_UNBANED);
                    }
                    playerMapper.changePlayerStatus(condition.getPlayerId(), '1', condition.getAppid());
                    operationRecord.setOperationType("解封");
                    playerMapper.insert(operationRecord);
                    log.info("用户操作已完成,解封");
                    break;
                }
                case "spec": {  //赋权
                    if (power.equals("0")) {
                        log.error("用户已是特殊用户，无需再次赋权");
                        throw new AppException(ResponseEnum.ALEREADY_SPECIAL);
                    }
                    playerMapper.changePlayerPower(condition.getPlayerId(), '0', condition.getAppid());
                    log.info("用户操作已完成,赋权");
                    break;
                }
                case "unspec": {  //撤权
                    if (power.equals("1")) {
                        log.error("用户已是普通用户，无需再次撤权");
                        return new ResultMessage(ResponseEnum.ALEREADY_UNSPECIAL, null);
                    }
                    playerMapper.changePlayerPower(condition.getPlayerId(), '1', condition.getAppid());
                    log.info("用户操作已完成,撤权");
                    break;
                }

            }
        } catch (AppException e) {
            SpringRollBackUtil.rollBack();
            log.error("针对用户的操作失败,请检查....", new AppException(e.getCode(), e.getMessage()));
            return new ResultMessage(e.getCode(), e.getMessage(), null);
        }

        return new ResultMessage(ResponseEnum.SUCCESS, null);
    }

}
