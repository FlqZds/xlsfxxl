package com.yunting.ws.Ws;

import com.google.gson.Gson;

import com.yunting.common.results.ResponseEnum;
import com.yunting.common.results.ResultMessage;
import com.yunting.common.utils.RedisUtil_Record;
import com.yunting.common.utils.RedisUtil_session;
import com.yunting.common.utils.ST;
import com.yunting.ws.SpringBeanContext;
import com.yunting.ws.config.websocket.MyConfigurator;
import com.yunting.ws.dto.PlayerMetaData;
import com.yunting.ws.entity.UserGatheringSetting;
import com.yunting.ws.utils.sessionUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.yunting.common.utils.FS.heartBeatMsg;
import static com.yunting.common.utils.FS.heartBeatRespMsg;

@Component("Wso")
@Slf4j
@ServerEndpoint(value = "/wso", configurator = MyConfigurator.class)
public class Wso {
    private static RedisUtil_session rus;
    private static RedisUtil_Record rur;
    private static sessionUtils gatheringUtil;
    private static ST st;
    private static Gson gson = new Gson();

    public static UserGatheringSetting gatheringSetting;//聚集设置变更的接收者
    public static String noticeMSG; //通知公告
    public static Boolean IsWeekend; //是否周末
    public static Boolean Forbidden; //是否禁止
    public static AtomicInteger castNum;//公告展示次数
    public static AtomicInteger setNum;//设置检测次数

    public static volatile ConcurrentHashMap<String, Session> SESSION_POOL = new ConcurrentHashMap<String, Session>();

    public Wso() {
//        //        自己从上下文中获取bean

        log.info("HeartDetect初始化中...");
        Wso.gatheringUtil = SpringBeanContext.getContext().getBean(sessionUtils.class);
        Wso.rus = SpringBeanContext.getContext().getBean(RedisUtil_session.class);
        Wso.rur = SpringBeanContext.getContext().getBean(RedisUtil_Record.class);
        st = SpringBeanContext.getContext().getBean(ST.class);


        gatheringSetting = new UserGatheringSetting();
        noticeMSG = "";
        castNum = new AtomicInteger(0);
        setNum = new AtomicInteger(0);
        log.info("HeartDetect初始化完成");
    }


    //顺序
    // 连接 --》 用户划走（异常） --》  断开
    @OnOpen
    public void onOpen(Session session) {
        String playerID = gatheringUtil.makeSessionFlag(session);
        String wxName = rus.hGet(playerID, "wxName").toString();

        log.info("玩家:" + "<|" + wxName + "|>" + "已登录.....");
        session.getAsyncRemote().sendText(heartBeatMsg);
        session.setMaxBinaryMessageBufferSize(100);
        session.setMaxTextMessageBufferSize(100);

        SESSION_POOL.putIfAbsent(playerID, session);

        rus.setEx(playerID + "State", "", 7, TimeUnit.SECONDS);

        String s;
        PlayerMetaData gameSetting = gatheringUtil.getGameSetting(playerID);
        s = gson.toJson(new ResultMessage(ResponseEnum.SUCCESS, gameSetting));
        session.getAsyncRemote().sendText(s);
        rus.sAdd("WEBSOCKET", playerID);

        rus.sRemove("DET", playerID); //从检测列表中移除

    }


    @OnMessage
    public void SendMessage(String message, Session session) throws IOException {
        log.info("收到消息:" + st.ADV_Interval());
        String playerID = gatheringUtil.thisSessionBelongsTo(session);
        String wxName = rus.hGet(playerID, "wxName").toString();

//在线用户的心跳
        if (message.equals(heartBeatRespMsg)) {
            log.info("ID:" + playerID + "| 昵称:" + wxName + "__" + heartBeatRespMsg);
            rus.expire(playerID + "State", 7, TimeUnit.SECONDS);//刷新存活态

            session.getAsyncRemote().sendText(heartBeatMsg);
        }

//管理端通知公告  让redis通知,此时有了通知公告
        if (castNum.get() > 0 && (noticeMSG.isEmpty() == false || noticeMSG.length() >= 3)) {
            log.info("有新的通知公告了" + noticeMSG);
            session.getAsyncRemote().sendText(noticeMSG);
            castNum.getAndDecrement();
        }


//让redis通知,在聚集设置改变的时候
// 获取聚集设置,并检测是否踢人
        if ((gatheringSetting != null) && setNum.get() > 0) {
            Character choice = gatheringSetting.getGatheringChoice(); //聚集选择项
            Integer pop_L = gatheringSetting.getGatheringPopulationLarge();//聚集大范围设置人数上限
            Integer pop_S = gatheringSetting.getGatheringPopulationSmall();//聚集小范围设置人数上限
            Integer deviceLimit = gatheringSetting.getGatheringDeviceLimit();//同型号设备数量上限
            Integer sameMacPopulation = gatheringSetting.getSameMacPopulation();//同mac同地址在线用户数量上限
//            rus.setMembers("WEBSOCKET");
            setNum.getAndDecrement();
            log.info("更新的聚集设置:" + gatheringSetting);
        }


// 十二点在线用户留存
        if (LocalDateTime.now().isEqual(LocalDateTime.now().withHour(16).withMinute(27)) && st.broadCount == 0) {
            log.info("十二点整了,该记录在线的玩家行为记录了");
            session.getAsyncRemote().sendText("西域春纯牛奶");
            st.broadCount++;//todo 这个过会儿要改一下变量
        }

    }


    @OnError
    public void onError(Session session, Throwable throwable) throws IOException {
        log.info(throwable.getMessage() + "");
        log.info(throwable.getCause() + "");
        String playerID = gatheringUtil.thisSessionBelongsTo(session);
        String location = rus.hGet(playerID, "pos").toString();
        gatheringUtil.userExit(playerID, location);
        log.info("池子的人:" + rus.setMembers("WEBSOCKET"));
    }

    @OnClose
    public void onClose(Session session) throws IOException {
//        String playerID = gatheringUtil.thisSessionBelongsTo(session);
//        String location = rus.hGet(playerID, "pos").toString();
//        gatheringUtil.userExit(playerID, location);
//        String wxName = rus.hGet(playerID, "wxName").toString();
////
//        gatheringUtil.userExit(playerID, location);
//
//        if (SESSION_POOL.contains(playerID)) {
//            SESSION_POOL.get(playerID).close();
//            SESSION_POOL.remove(playerID);
//            log.info("玩家:" + "<|" + wxName + "|>" + "已断开连接...");
//        }
//        log.info("玩家:" + "<|" + wxName + "|>" + "已断开连接...");
//        int size = SESSION_POOL.size();
//        log.info("当前池子有" + size + "个用户");
//        log.info("池子的人:" + SESSION_POOL.values());

    }

}
