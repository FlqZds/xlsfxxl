package com.yunting.ws.Ws;


import com.google.gson.Gson;
import com.yunting.common.results.ResponseEnum;
import com.yunting.common.results.ResultMessage;
import com.yunting.common.utils.RedisUtil_Record;
import com.yunting.common.utils.RedisUtil_session;
import com.yunting.common.utils.ST;
import com.yunting.ws.utils.sessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.Session;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.yunting.common.utils.FS.serverHeartBearDuration;
import static com.yunting.ws.Ws.Wso.SESSION_POOL;

@Component("HeartBongBongBong")
@Slf4j
public class HeartBongBongBong {

    @Resource(name = "RedisUtil_session")
    private RedisUtil_session rus;

    @Resource(name = "GatheringUtils")
    private sessionUtils gatheringUtil;

    //手动管理退出连接
//    @Scheduled(fixedDelay = serverHeartBearDuration * 3 * 1000) // 每3秒执行一次
//    public void cleanWasteUser() throws IOException {
//        for (String playerID : rus.setMembers("WEBSOCKET")) {
//            log.info("开始检查池里的僵尸_要检查的次数==>:" + rus.setMembers("WEBSOCKET").size());
//            String wxName = rus.hGet(playerID, "wxName").toString();
//            Long expire = rus.getExpire(playerID + "State");
//
//            //连过websocket,但很久未pong的僵尸
//            if (expire <= 1) {
//                rus.sendMessage("my_channel", "ID:" + playerID + " |-_-| nickName:" + wxName + "  is  Exit");
//                log.info("ID:" + playerID + "|-__-|昵称:" + wxName + "太久未pong,现已退出连接");
//                if (SESSION_POOL.keySet().contains(playerID)) {
//                    SESSION_POOL.get(playerID).close();
//                }
//                String location = rus.hGet(playerID, "pos").toString();
//                gatheringUtil.userExit(playerID, location);
//                SESSION_POOL.remove(playerID);
//            }
//        }
//
//        //检查未连接websocket的聚集
//        for (String s : rus.setMembers("DET")) {
//            log.info("开始检查未连接websocket的聚集");
//            Long expire = rus.getExpire(s + "DET");
//            if (expire <= 3) {
//                rus.sRemove("DET", s);
//                String del_location = rus.get(s + "DET");
//                gatheringUtil.userExit(s, del_location);
//                log.info("ID:" + s + "的玩家|-__-|" + "聚集验证通过后,太久未连接websocket,现已从redis中移除...");
//            }
//        }
//    }

    @Resource(name = "ST")
    private ST st;

    @Resource(name = "RedisUtil_Record")
    private RedisUtil_Record rur;

    //    @Scheduled(fixedDelay = 60 * 60 * 1000) // 每3秒执行一次
//    public void broadCastPlayerWatchAdv() {
//        this.getAdminForbitAdvSetting();
//
//        int broadCount = Integer.parseInt(rur.get("broadCount"));
//        Gson gson = new Gson();
//        if (st.Forbid_Begin_Time() == LocalDateTime.now().getHour() && broadCount <= 3) {
//            log.info("开头是否发消息:次数" + broadCount);
//            for (String member : rus.setMembers("WEBSOCKET")) {
//                Session session = SESSION_POOL.get(member);
//                String isSeeAdv = rur.get("isSeeAdv");
//                if (isSeeAdv.equals("true")) {
//                    session.getAsyncRemote().sendText(gson.toJson(new ResultMessage(ResponseEnum.NOTICE_ENABLE_WATCH_ADV, true)));
//                } else {
//                    session.getAsyncRemote().sendText(gson.toJson(new ResultMessage(ResponseEnum.NOTICE_ENABLE_WATCH_ADV, false)));
//                }
//                rur.incrBy("broadCount", 1);
//            }
//        }
//
//        if (st.Forbid_End_Time() == (LocalDateTime.now().getHour()) && broadCount <= 7 && broadCount >= 5) {
//            log.info("结尾是否发消息");
//            for (String member : rus.setMembers("WEBSOCKET")) {
//                String isSeeAdv = rur.get("isSeeAdv");
//                Session session = SESSION_POOL.get(member);
//                if (isSeeAdv.equals("true")) {
//                    session.getAsyncRemote().sendText(gson.toJson(new ResultMessage(ResponseEnum.NOTICE_ENABLE_WATCH_ADV, true)));
//                } else {
//                    session.getAsyncRemote().sendText(gson.toJson(new ResultMessage(ResponseEnum.NOTICE_ENABLE_WATCH_ADV, false)));
//                }
//                rur.incrBy("broadCount", 1);
//            }
//        }
//
//    }

//    public void getAdminForbitAdvSetting() {
//        log.info("开始读取禁止观看广告设置");
//        if (st.isForbid_Switch() == false) {
//            rur.set("isSeeAdv", "true");
//        } else {//开启了
//            rur.set("isSeeAdv", "false");
//
//            //每天的该时间段可以看广告
//            if (LocalDateTime.now().withHour(st.Forbid_Begin_Time()).isAfter(LocalDateTime.now()) ||
//                    LocalDateTime.now().withHour(st.Forbid_End_Time()).isBefore(LocalDateTime.now())) {
//                rur.set("isSeeAdv", "true");
//            }
//
//            //周末也开了,就是周末全天都可以看广告
//            if (st.IS_Weekend()) {
//                if (LocalDate.now().getDayOfWeek().getValue() == 6 || LocalDate.now().getDayOfWeek().getValue() == 7) {
//                    rur.set("isSeeAdv", "true");
//                }
//            }
//        }
//        st.broadCount = 0;
//        log.info("当日当前时间段是否可以看广告已更新");
//    }

}


