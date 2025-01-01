package com.yunting.login.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.yunting.common.exception.AppException;
import com.yunting.common.results.ResponseEnum;
import com.yunting.common.results.ResultMessage;
import com.yunting.common.utils.ST;
import com.yunting.login.entity.Player;
import com.yunting.login.forest.ForestService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Component("WxRequestUtil")
public class WxRequestUtil {
    private String wxAvatar; //微信头像
    private String nickname;   //微信昵称
    private String accessToken;
    private String openid;
    private Long applicationId;

    @Resource(name = "ForestService")
    private ForestService forestService;

    @Resource(name = "ST")
    private ST st;

    public String getWxOpenID(String packageName, String wxCode) {
        String appInfo_json = forestService.getAppInfo(packageName);//请求后台 拿到appid和appSecret
        Map<String, String> parse_app = (Map<String, String>) JSON.parse(appInfo_json);
        if (parse_app.get("code").equals("200") == false || parse_app.size() <= 0 || StringUtils.isEmpty(appInfo_json)) {
            log.error("请求后台获取app信息失败");
            throw new AppException(ResponseEnum.SELECT_APPLICATION_FAILED);
        }
        ResultMessage resultMessage = JSONObject.parseObject(appInfo_json, ResultMessage.class);

        Map<String, String> parse_app_map = (Map<String, String>) resultMessage.getData();

        String appid = parse_app_map.get("appid");
        String appSecret = parse_app_map.get("appSecret");

        //请求微信拿到accessToken
        String accessToken_json = forestService.getAccessTokenBycode(appid, appSecret, wxCode);
        Map<String, String> parse_accessToken = (Map<String, String>) JSON.parse(accessToken_json);
        if (parse_accessToken.size() <= 0 || StringUtils.isEmpty(accessToken_json) || StringUtils.isEmpty(parse_accessToken.get("access_token")) == true || StringUtils.isEmpty(parse_accessToken.get("openid")) == true) {
            log.error("请求微信token失败");
            throw new AppException(ResponseEnum.REQUEST_ACCESS_TOKEN_FAILED);
        }


        accessToken = parse_accessToken.get("access_token");
        openid = parse_accessToken.get("openid");  // 微信openID
        return openid;
    }


    /*到这里就是新用户 ,要从微信中请求数据注册 而不是从数据库中取数据
        请求微信拿到用户信息*/
    public Player getWxUser(String proxyName, Long mobileID) {
        String wx_info_json = forestService.getWx_InfoByToken(accessToken, openid);
        Map<String, String> parse_wxInfo = (Map<String, String>) JSON.parse(wx_info_json);
        if (parse_wxInfo.size() <= 0 ||
                StringUtils.isEmpty(wx_info_json) ||
                StringUtils.isEmpty(parse_wxInfo.get("nickname")) == true ||
                StringUtils.isEmpty(parse_wxInfo.get("headimgurl")) == true) {
            log.error("获取微信用户信息失败");
            throw new AppException(ResponseEnum.REQUEST_WECHAT_USERINFO_FAILED);
        }
        wxAvatar = parse_wxInfo.get("headimgurl"); //微信头像
        nickname = parse_wxInfo.get("nickname");   //微信昵称

        applicationId = Long.parseLong(st.GameId());

        Player player = Player.builder().
                gameId(applicationId).
                playerCreatTime(LocalDateTime.now()). //玩家创建时间
                wxOpenId(openid).       //微信openID
                status('1').            //用户状态 （是否被封禁 0是封禁用户，1是正常用户 ）
                wxNickname(nickname).   //昵称
                special("1").           //特殊用户  0是特殊用户,1是正常用户
                wxHeadimgurl(wxAvatar). //头像
                mobileId(mobileID).     //设备信息ID
                inRed(BigDecimal.ZERO). //红包余额
                proxyName(proxyName).   //标识
                build();

        return player;
    }
}
