package com.yunting.login.controller;

import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.PageInfo;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yunting.common.Dto.PlayerDTO;
import com.yunting.common.Dto.RiskControlSetting;
import com.yunting.common.results.ResponseEnum;
import com.yunting.common.results.ResultMessage;
import com.yunting.common.utils.IpUtils;
import com.yunting.common.utils.ST;
import com.yunting.login.dto.PlayerMetaData;
import com.yunting.login.dto.SignDto;
import com.yunting.login.dto.WithdrawVo;
import com.yunting.login.entity.DeviceBrand;
import com.yunting.login.entity.MobileDetail;
import com.yunting.login.service.LoginService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/Client")
public class ClientController {
    @Resource(name = "LoginService")
    private LoginService loginService;

    @Resource(name = "ST")
    private ST st;

    //    用户登录 \  注册
    @ApiOperation(value = "微信登录,上传设备记录由服务端进行验证,获取风控参数")
    @PostMapping("/signOn")
    public ResultMessage sendUserInfo(HttpServletRequest request, @RequestBody SignDto signDto) {
        ResultMessage resultMessage = loginService.PlayerSignOn(request, signDto);
        return resultMessage;
    }

    @ApiOperation(value = "获取游戏数据+玩家数据")
    @PostMapping("/gameSetting")
    public ResultMessage getGameSetting(@ApiIgnore @RequestAttribute("playerDTO") PlayerDTO playerDTO) {
        PlayerMetaData playerMetaData = loginService.getGameSetting(playerDTO);
        return new ResultMessage(ResponseEnum.SUCCESS, playerMetaData);
    }

    @ApiOperation(value = "获取风控参数")
    @GetMapping("/risking")
    public ResultMessage getRiskControlSetting(HttpServletRequest request) {
        RiskControlSetting risk = st.Risk();
        String ip = IpUtils.getIpAddr(request);
        String addr = IpUtils.getCityInfo(ip);
        JSONObject of = JSONObject.of("address", addr, "riskControlSetting", risk);
        log.info("风控参数已返回" + risk);
        return new ResultMessage(ResponseEnum.SUCCESS, of);
    }


    @ApiOperation(value = "获取该玩家的所有提现记录")
    @PutMapping("/cashrd/{pageNum}")
    public ResultMessage getRiskControlSetting(@ApiIgnore @RequestAttribute("playerDTO") PlayerDTO playerDTO,
                                               @PathVariable("pageNum") Integer pageNum) {
        PageInfo pageInfo = loginService.getPlayerWithdrawRecord(playerDTO, pageNum);
        return new ResultMessage(ResponseEnum.SUCCESS, pageInfo);
    }

//    @ApiOperation(value = "手动添加设备信息")
//    @PostMapping("/addMobile")
//    public ResultMessage addMobile(@RequestBody MobileDetail mobileDetail) {
//
//        updateService.addMobile(mobileDetail);
//        return new ResultMessage(ResponseEnum.SUCCESS, null);
//
//    }

    @ApiOperation(value = "采集并上传,返回允许登录设备表")
    @PostMapping("/collection")
    private ResultMessage uploadMobileInfo(@RequestBody MobileDetail mobileDetail) {

        DeviceBrand brandInfo = loginService.collectionAndUploadMobileInfo(mobileDetail);
        return new ResultMessage(ResponseEnum.SUCCESS, brandInfo);
    }



}


