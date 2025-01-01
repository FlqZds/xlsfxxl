package com.yunting.ws.config.websocket;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Component("WebSocketConfig")
@SpringBootConfiguration
public class WebSocketConfig {

    //         * 初始化端点对象,也就是被@ServerEndpoint所标注的对象
    @Bean("MyServerEndpointExporter")
    public ServerEndpointExporter getServerEndpointExporter() {

        return new ServerEndpointExporter();
    }

}
