package com.yunting.client.common.config.websocket;

import com.yunting.client.Ws.Wso;
import com.yunting.client.common.config.redis.RedisMessageListener;
import com.yunting.client.common.utils.RedisUtil_session;
import com.yunting.client.common.utils.sessionUtils;
import com.yunting.client.mapper.DayBehaveRecordlistMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.annotation.Resource;

@Component("WebSocketConfig")
@SpringBootConfiguration
public class WebSocketConfig {

    //         * 初始化端点对象,也就是被@ServerEndpoint所标注的对象
    @Bean("MyServerEndpointExporter")
    public ServerEndpointExporter getServerEndpointExporter() {

        return new ServerEndpointExporter();
    }

}
