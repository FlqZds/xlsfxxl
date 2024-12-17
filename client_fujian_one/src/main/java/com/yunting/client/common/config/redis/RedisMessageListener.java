package com.yunting.client.common.config.redis;

import com.google.gson.Gson;
import com.yunting.client.Ws.Wso;
import com.yunting.client.entity.setting.UserGatheringSetting;
import com.yunting.common.results.ResponseEnum;
import com.yunting.common.results.ResultMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component("RedisMessageListener")
public class RedisMessageListener implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {

        String channel = new String(message.getChannel());
        String body = message.toString();
        log.info("接收到频道 [" + channel + "] 的消息：" + body);
        body = body.substring(body.indexOf(":") + 1);

        Gson gson = new Gson();
        if (channel.equals("notice_msg") && (body != null || !body.equals(""))) {
            String s = gson.toJson(new ResultMessage(ResponseEnum.NOTICE_MSG_MODIFYED, body));
            Wso.noticeMSG = s;
            Wso.castNum.set(3);
        }

        if (channel.equals("setting") && (body != null || !body.equals(""))) {
            UserGatheringSetting userGatheringSetting = gson.fromJson(message.toString(), UserGatheringSetting.class);
            Wso.gatheringSetting.setGatheringChoice(userGatheringSetting.getGatheringChoice()); //变更的聚集选择项
            Wso.gatheringSetting.setGatheringDeviceLimit(userGatheringSetting.getGatheringDeviceLimit());//同型号数量上限
            //聚集大范围设置人数上限
            Wso.gatheringSetting.setGatheringPopulationLarge(userGatheringSetting.getGatheringPopulationLarge());
            //聚集小范围设置人数上限
            Wso.gatheringSetting.setGatheringPopulationSmall(userGatheringSetting.getGatheringPopulationSmall());
            //允许同一个mac+同地址在线用户数量
            Wso.gatheringSetting.setSameMacPopulation(userGatheringSetting.getSameMacPopulation());
            Wso.setNum.set(2);
        }

    }
}
