package com.yunting.client.Controller;


import cn.hutool.core.net.Ipv4Util;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.yunting.client.DTO.VO.infoVO;
import com.yunting.client.DTO.dataTransFer.PlayerMetaData;
import com.yunting.client.DTO.dataTransFer.SignDto;
import com.yunting.client.entity.MobileDetail;
import com.yunting.client.entity.setting.RiskControlSetting;
import com.yunting.clientservice.ClientImpl;
import com.yunting.clientservice.service.ClientService;
import com.yunting.common.Dto.PlayerDTO;
import com.yunting.common.results.ResponseEnum;
import com.yunting.common.results.ResultMessage;
import com.yunting.common.utils.IpUtils;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.Null;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/Client")
@Api(tags = "客户端接口")
@Slf4j
public class ClientController {
    @Resource(name = "ClientService")
    private ClientService clientService;

    @Resource(name = "ClientService")
    private ClientImpl client;

    //    http://localhost:8088/Client/testing?playerId=123
    //专用 测试 请求
    @ApiOperation(value = "专用 测试 请求")
    @GetMapping("/testing")
    private String testing(HttpServletRequest request, @RequestParam("playerId") String playerId) throws ServletException, IOException {

        if (ObjectUtil.isEmpty(request)) {
            return Ipv4Util.LOCAL_IP;
        } else {
//            try {
////                String remoteHost = JakartaServletUtil.getClientIP(request);
//                return "0:0:0:0:0:0:0:1".equals(remoteHost) ? Ipv4Util.LOCAL_IP : remoteHost;
//            } catch (Exception e) {
//                log.error(">>> 获取客户端ip异常：", e);
//                return Ipv4Util.LOCAL_IP;
//            }
        }
        return "0:0:0:0:0:0:0:1";
    }


    @ApiOperation(value = "手动添加设备信息")
    @PostMapping("/addMobile")
    public ResultMessage addMobile(@RequestBody MobileDetail mobileDetail) {

        client.addMobile(mobileDetail);
        return new ResultMessage(ResponseEnum.SUCCESS, null);

    }


    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功-SUCCESSFUL",responseContainer = "List", response = String.class),
            @ApiResponse(code = 500, message = "发生未知异常，请联系管理员"),
            @ApiResponse(code = 55099, message = "添加手机详细信息出错")
    })
    @ApiOperation(value = "采集并上传,返回设备不允许登录设备表")
    @PostMapping("/collection")
    private ResultMessage uploadMobileInfo(@RequestBody MobileDetail mobileDetail) {

        List<String> strings = client.collectionAndUploadMobileInfo(mobileDetail);
        return new ResultMessage(ResponseEnum.SUCCESS, strings);
    }


    //    用户登录 \  注册
    @ApiOperation(value = "微信登录,上传设备记录由服务端进行验证,获取风控参数")
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功", response = PlayerDTO.class),
            @ApiResponse(code = 500, message = "请求失败"),
            @ApiResponse(code = 80504, message = "您的账号已被封禁，请联系管理员"),
            @ApiResponse(code = 80401, message = "用户未登录,请先登录"),
            @ApiResponse(code = 80503, message = "用户注册失败"),
            @ApiResponse(code = 80545, message = "无效Token ,请使用正确的Token"),

    })
    @PostMapping("/signOn")
    public ResultMessage sendUserInfo(HttpServletRequest request,@RequestBody SignDto signDto) throws Exception {
        abv = 12;
        abvlist.add("1");
        log.info("大小:" + abv);
        log.info("长度:" + abvlist);

        ResultMessage resultMessage = client.PlayerSignOn(request,signDto);
        return resultMessage;
    }

    public static Integer abv = 0;
    public static List abvlist = new ArrayList<String>();

    @ApiOperation(value = "聚集验证和同型号验证")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "location", value = "位置信息", required = true, paramType = "RequestBody", dataType = "String"),
            @ApiImplicitParam(name = "Wifi", value = "wifi列表", required = true, paramType = "RequestBody", dataType = "String"),
            @ApiImplicitParam(name = "thisMAC", value = "该玩家自己mac", required = true, paramType = "RequestBody", dataType = "String")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功-SUCCESSFUL", response = Null.class),
            @ApiResponse(code = 80595, message = "在线人数超出聚集设置上限,请重新登录"),
            @ApiResponse(code = 66028, message = "该设备型号数量已达上限,请稍后再试"),
            @ApiResponse(code = 66029, message = "该MAC地址人数已达上限,请稍后再试"),
            @ApiResponse(code = 80504, message = "您的账号已被封禁，请联系管理员"),
            @ApiResponse(code = 66027, message = "登录异常,请检查设备是否更换"),
            @ApiResponse(code = 500, message = "发生未知异常，请联系管理员"),
    })
    @PostMapping("/lIdetfy")
    public ResultMessage gathIdentifyAndTypeIdentify(@ApiIgnore @RequestAttribute PlayerDTO playerDTO, @ApiIgnore @RequestBody Map<String, String> json) {
        Map<String, String> thismap = JSON.to(Map.class, json);
        String thisMAC = thismap.get("thisMAC");
        String Wifi_json = thismap.get("Wifi");
        List<String> Wifi = null;
        if (Objects.nonNull(Wifi_json)) {
            Wifi = JSON.parseArray(Wifi_json, String.class);
        }
        ResultMessage resultMessage = client.identification(playerDTO, thisMAC, Wifi, null);
        return resultMessage;
    }


    @ApiOperation(value = "通过包名获取游戏设置")
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功-SUCCESSFUL", response = PlayerMetaData.class),
            @ApiResponse(code = 500, message = "发生未知异常，请联系管理员"),
            @ApiResponse(code = 404, message = "未存在的资源请求"),
            @ApiResponse(code = 401, message = "未身份验证的请求"),
            @ApiResponse(code = 403, message = "未授权的请求"),
            @ApiResponse(code = 66155, message = "查询游戏设置失败"),

    })
    @GetMapping("/gameSetting")
    public ResultMessage getGameSetting(@ApiIgnore @RequestAttribute("playerDTO") PlayerDTO playerDTO, @RequestParam("packageName") String packageName) {

        PlayerMetaData playerMetaData = clientService.getGameSetting(playerDTO, packageName);
        return new ResultMessage(ResponseEnum.SUCCESS, playerMetaData);
    }


    @ApiOperation(value = "通过包名获取风控参数")
    @ApiImplicitParam(name = "packageName", value = "包名", required = true, dataType = "String")
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功-SUCCESSFUL", response = RiskControlSetting.class),
            @ApiResponse(code = 66411, message = "查询风控设置失败"),
    })

    @GetMapping("/risking")
    public ResultMessage getRiskControlSetting(HttpServletRequest request,@RequestParam("packageName") String packageName) {

        RiskControlSetting riskControlSetting = clientService.getRiskControlSetting(packageName);
        String ip = IpUtils.getIpAddr(request);
        log.info("该请求的ip:" + ip);
        String addr = IpUtils.getCityInfo(ip);
        log.info("该请求的地址:" + addr);
        JSONObject of = JSONObject.of("address", addr, "riskControlSetting", riskControlSetting);
        return new ResultMessage(ResponseEnum.SUCCESS, of);
    }


    @ApiOperation(value = "上传设备记录和地址由服务端进行设备验证和聚集验证")
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功-SUCCESSFUL"),
            @ApiResponse(code = 80595, message = "在线人数超出聚集设置上限,请重新登录"),
            @ApiResponse(code = 66028, message = "该设备型号数量已达上限,请稍后再试"),
            @ApiResponse(code = 66029, message = "该MAC地址人数已达上限,请稍后再试"),
            @ApiResponse(code = 80504, message = "您的账号已被封禁，请联系管理员"),
            @ApiResponse(code = 66027, message = "登录异常,请检查设备是否更换"),
            @ApiResponse(code = 500, message = "发生未知异常，请联系管理员"),
    })
    @PostMapping("/uIdetfy")
    public ResultMessage loginIdentify(@ApiIgnore @RequestAttribute PlayerDTO playerDTO, @RequestBody infoVO json) {

        ResultMessage resultMessage = client.storeDeviceAndLocationThenIdentify(playerDTO, json);
        return resultMessage;
    }

}
