package com.yunting.client.Ws;

import com.google.gson.Gson;
import com.yunting.SpringBeanContext;
import com.yunting.client.common.config.redis.RedisMessageListener;
import com.yunting.client.common.config.websocket.MyConfigurator;
import com.yunting.client.common.results.ResponseEnum;
import com.yunting.client.common.results.ResultMessage;
import com.yunting.client.common.utils.RedisUtil_session;
import com.yunting.client.common.utils.sessionUtils;
import com.yunting.client.entity.setting.GameSetting;
import com.yunting.client.entity.setting.UserGatheringSetting;
import com.yunting.client.mapper.DayBehaveRecordlistMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.yunting.client.common.utils.FS.heartBeatMsg;
import static com.yunting.client.common.utils.FS.heartBeatRespMsg;

@Component("Wso")
@Slf4j
@AllArgsConstructor
@ServerEndpoint(value = "/HeartDetect", configurator = MyConfigurator.class)
public class Wso {
    private static RedisUtil_session rus;
    private static sessionUtils gatheringUtil;
    public static DayBehaveRecordlistMapper dayBehaveMapper;
    public static RedisMessageListener listener;
    private static Gson gson = new Gson();

    public static UserGatheringSetting gatheringSetting;//聚集设置变更的接收者
    public static String noticeMSG; //通知公告
    public static Boolean IsWeekend; //是否周末
    public static Boolean Forbidden; //是否禁止
    public static AtomicInteger castNum;//公告展示次数
    public static AtomicInteger setNum;//设置检测次数

    public static volatile ConcurrentHashMap<String, Session> SESSION_POOL = new ConcurrentHashMap();

    //    @Autowired
    public Wso() {
//        //        自己从上下文中获取bean

        log.info("HeartDetect初始化中...");
        Wso.gatheringUtil = SpringBeanContext.getContext().getBean(sessionUtils.class);
        Wso.rus = SpringBeanContext.getContext().getBean(RedisUtil_session.class);
        Wso.dayBehaveMapper = SpringBeanContext.getContext().getBean(DayBehaveRecordlistMapper.class);
        Wso.listener = SpringBeanContext.getContext().getBean(RedisMessageListener.class);


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
        session.setMaxTextMessageBufferSize(1024);

        SESSION_POOL.putIfAbsent(playerID, session);

        rus.setEx(playerID + "State", "", 10, TimeUnit.SECONDS);
        String s = gson.toJson(new ResultMessage(ResponseEnum.SUCCESS, null));
        session.getAsyncRemote().sendText(s);
        rus.expire(playerID + "State", 10, TimeUnit.SECONDS);//刷新存活态
        rus.sAdd("WEBSOCKET", playerID);

        rus.sRemove("DET", playerID); //从检测列表中移除

    }

    private Integer a = 0;

    @OnMessage
    public void SendMessage(String message, Session session) throws IOException {
        String playerID = gatheringUtil.thisSessionBelongsTo(session);
        String wxName = rus.hGet(playerID, "wxName").toString();

//在线用户的心跳
        if (message.equals(heartBeatRespMsg)) {
            log.info("ID:" + playerID + "| 昵称:" + wxName + "__" + heartBeatRespMsg);
            rus.expire(playerID + "State", 10, TimeUnit.SECONDS);//刷新存活态

            session.getAsyncRemote().sendText(heartBeatMsg);
        }

        Session sessiont = SESSION_POOL.get(playerID);
        log.info("是否为空:" + Objects.isNull(sessiont));

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
        if (LocalDateTime.now().isEqual(LocalDateTime.now().withHour(00).withMinute(00)) && a == 0) {
            log.info("十二点整了,该记录在线的玩家行为记录了");
            session.getAsyncRemote().sendText("西域春纯牛奶");
            a++;
        }

//到点不准看广告+周末限制时间段
        if (LocalDateTime.now().isEqual(LocalDateTime.now().withHour(00).withMinute(00)) && a == 0) {
            log.info("十二点整了,该记录在线的玩家行为记录了");
            session.getAsyncRemote().sendText("西域春牛奶");
            a++;
        }

    }


    @OnError
    public void onError(Session session, Throwable throwable) throws IOException {
        log.info(throwable.getMessage() + "");
        log.info(throwable.getCause() + "");
        log.info("池子的人:" + rus.setMembers("WEBSOCKET"));
    }

    @OnClose
    public void onClose(Session session) throws IOException {
//        String playerID = gatheringUtil.thisSessionBelongsTo(session);
//        String wxName = rus.hGet(playerID, "wxName").toString();
//        String location = rus.hGet(playerID, "pos").toString();
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


    /**
     * 获取用户聚集设置
     *
     * @param sessionInfo
     * @return
     * @throws IOException
     */
    /*
    private UserGatheringSetting getUserLocationSetting(UserSession userSession) throws IOException {
        //拿到聚集设置
        String UserGatheringSetting_json = forest.getUserGatheringSetting(userSession.gameID);
        ResultMessage resultMessage = JSONObject.parseObject(UserGatheringSetting_json, ResultMessage.class);
        UserGatheringSetting gathering = JSON.to(UserGatheringSetting.class, resultMessage.getData());
//        log.info("游戏{>" + gameId + "<}的聚集设置已读取:" + gathering);
        if (Objects.isNull(gathering)) {
            log.error("聚集设置读取失败,请检查" + userSession.gameID + "的gameID是否正确");
            throw new AppException(ResponseEnum.GET_USER_GATHERING_SETTING_FAILED);
        }
        return gathering;
    }
     */

}
