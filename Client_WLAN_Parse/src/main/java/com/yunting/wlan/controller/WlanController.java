package com.yunting.wlan.controller;

import com.alibaba.fastjson2.JSON;
import com.yunting.common.Dto.PlayerDTO;
import com.yunting.common.results.ResultMessage;
import com.yunting.wlan.dto.DeviceDTO;
import com.yunting.wlan.dto.infoVO;
import com.yunting.wlan.service.WlanService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.Null;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/wlan")
@Slf4j
public class WlanController {

    @Resource(name = "WlanService")
    private WlanService wlanService;

    @ApiOperation(value = "聚集验证和同型号验证")
    @PostMapping("/lIdetfy")
    public ResultMessage gathIdentifyAndTypeIdentify(@ApiIgnore @RequestAttribute PlayerDTO playerDTO, @ApiIgnore @RequestBody Map<String, String> json) {
        Map<String, String> thismap = JSON.to(Map.class, json);
        String thisMAC = thismap.get("thisMAC");
        String Wifi_json = thismap.get("Wifi");
        List<String> Wifi = null;
        if (Objects.nonNull(Wifi_json)) {
            Wifi = JSON.parseArray(Wifi_json, String.class);
        }
        ResultMessage resultMessage = wlanService.identification(playerDTO, thisMAC, Wifi, null);
        return resultMessage;
    }

    @ApiOperation(value = "激励聚集验证接口,返回激励广告的记录id+相关省份")
    @PostMapping("/ecIdety")
    public ResultMessage identifyAndGetEncourageID(HttpServletRequest request, @ApiIgnore @RequestAttribute PlayerDTO playerDTO, @RequestBody DeviceDTO json) {

        ResultMessage resultMessage = wlanService.identifyAndGetEncourageID(request, playerDTO, json);
        return resultMessage;
    }

    @ApiOperation(value = "上传设备记录和地址由服务端进行设备验证和聚集验证")
    @PostMapping("/uIdetfy")
    public ResultMessage loginIdentify(@ApiIgnore @RequestAttribute PlayerDTO playerDTO, @RequestBody infoVO json) {

        ResultMessage resultMessage = wlanService.storeDeviceAndLocationThenIdentify(playerDTO, json);
        return resultMessage;
    }
}
