
package com.yunting.client.Ws;


import com.yunting.client.common.utils.RedisUtil_session;
import com.yunting.client.common.utils.sessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

import static com.yunting.client.Ws.Wso.SESSION_POOL;
import static com.yunting.client.common.utils.FS.serverHeartBearDuration;

@Component("HeartBongBongBong")
@Slf4j
public class HeartBongBongBong {

    @Resource(name = "RedisUtil_session")
    private RedisUtil_session rus;

    @Resource(name = "GatheringUtils")
    private sessionUtils gatheringUtil;

    //手动管理退出连接
    @Scheduled(fixedDelay = serverHeartBearDuration * 3 * 1000) // 每3秒执行一次
    public void cleanWasteUser() throws IOException {
        for (String playerID : rus.setMembers("WEBSOCKET")) {
            log.info("开始检查池里的僵尸_要检查的次数==>:" + rus.setMembers("WEBSOCKET").size());
            String wxName = rus.hGet(playerID, "wxName").toString();
            Long expire = rus.getExpire(playerID + "State");

            //连过websocket,但很久未pong的僵尸
            if (expire <= 1) {
                rus.sendMessage("my_channel", "ID:" + playerID + " |-_-| nickName:" + wxName + "  is  Exit");
                log.info("ID:" + playerID + "|-__-|昵称:" + wxName + "太久未pong,现已退出连接");
                if (SESSION_POOL.keySet().contains(playerID)) {
                    SESSION_POOL.get(playerID).close();
                }
                String location = rus.hGet(playerID, "pos").toString();
                gatheringUtil.userExit(playerID, location);
                SESSION_POOL.remove(playerID);
            }
        }

        //检查未连接websocket的聚集
        for (String s : rus.setMembers("DET")) {
            log.info("开始检查未连接websocket的聚集");
            Long expire = rus.getExpire(s + "DET");
            if (expire <= 3) {
                rus.sRemove("DET", s);
                String del_location = rus.get(s + "DET");
                gatheringUtil.userExit(s, del_location);
                log.info("ID:" + s + "的玩家|-__-|" + "聚集验证通过后,太久未连接websocket,现已从redis中移除...");
            }
        }
    }


//    //向所有在线用户广播消息
//    @Scheduled(fixedDelay = serverHeartBearDuration * 8 * 1000) // 每8秒执行一次
//    public void broadcastMSG() {
//        for (String member : rus.setMembers("WEBSOCKET")) {
//            Session session = SESSION_POOL.get(member);
//
//            if (Objects.nonNull(session) && session.isOpen() == true) {
//                Gson gson = new Gson();
//                String s = gson.toJson(new ResultMessage(ResponseEnum.SUCCESS, null));
////                session.getAsyncRemote().sendText(s);
//                log.info("消息已广播");
//            }
//        }
//    }

}


