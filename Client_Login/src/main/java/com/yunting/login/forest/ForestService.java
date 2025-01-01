package com.yunting.login.forest;

import com.dtflys.forest.annotation.Get;
import org.springframework.stereotype.Service;

@Service("ForestService")
public interface ForestService {

    //    纯测试的
    //    通过微信code获取用户信息
    @Get(url = "http://localhost:8080/Client/testing?playerId=123")
    String getUserInfo();

    @Get(url = "https://httpbin.org/ip")
    String getIp();

    @Get(url = "https://ipecho.net/plain")
    String getIp_temp();

//    向微信发请求

    //    通过微信code获取用户信息
    @Get(url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=${0}&secret=${1}&code=${2}&grant_type=authorization_code", sslProtocol = "TLS")
    String getAccessTokenBycode(String appid, String secret, String code);

    //    通过accessToken获取用户信息
    @Get(url = "https://api.weixin.qq.com/sns/userinfo?access_token=${0}&openid=${1}", sslProtocol = "TLS")
    String getWx_InfoByToken(String accessToken, String openid);

//    向平台发请求

    //    通过包名获取appid + secret

    @Get(url = "http://localhost:9999/application/getAppInfo?packageName=${0}")
    String getAppInfo(String packageName);


}
