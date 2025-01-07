package com.yunting.pay.controller;

import com.alibaba.fastjson2.JSON;
import com.yunting.common.Dto.PlayerDTO;
import com.yunting.common.results.ResponseEnum;
import com.yunting.common.results.ResultMessage;
import com.yunting.pay.services.PayServices;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@Api(tags = "提现接口")
@RequestMapping("/Withdraw")
public class PayController {

    @Resource(name = "PayServices")
    private PayServices payServices;

    @GetMapping("cl/ww")
    public ResultMessage applyWithdraw(@RequestParam String info) {
        return new ResultMessage(ResponseEnum.SUCCESS, info);
    }

    @ApiOperation(value = "提现申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "payId", value = "支付宝登录号", required = true),
            @ApiImplicitParam(name = "realName", value = "姓名", required = true),
            @ApiImplicitParam(name = "from", value = "提现来源", required = true)

    })
    @PostMapping("/cl/apy")
    public ResultMessage applyWithdraw(@ApiIgnore @RequestAttribute("playerDTO") PlayerDTO playerDTO, @ApiIgnore @RequestBody String info) {

        Long playerId = playerDTO.getPlayerId();

        HashMap<String, String> to = (HashMap<String, String>) JSON.to(Map.class, info);
        String payId = to.get("payId");
        String realName = to.get("realName");
        String from = to.get("from"); //提现来源from  [r=红包提现 o=订单提现]
        ResultMessage resultMessage = payServices.applyWithdraw(playerId, payId, realName, from);
        return resultMessage;
    }

    //    @ApiOperation(value = "绑定支付宝")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "payId", value = "支付宝登录号", required = true),
//            @ApiImplicitParam(name = "realName", value = "实名", required = true)
//    })
//    @PostMapping("/cl/manfy")
//    public ResultMessage manIdentify(@ApiIgnore @RequestAttribute("playerDTO") PlayerDTO playerDTO, @ApiIgnore @RequestBody String info) {
//        Long playerId = playerDTO.getPlayerId();
//
//        HashMap<String, String> to = (HashMap<String, String>) JSON.to(Map.class, info);
//        String payId = to.get("payId");
//        String realName = to.get("realName");
//
//        Integer integer = payServices.manIdentify(playerId, payId, realName);
//        if (integer != 1) {
//            return new ResultMessage(ResponseEnum.MAN_IDENTIFY_FAILED, null);
//        }
//        log.info("支付宝绑定成功");
//        return new ResultMessage(ResponseEnum.SUCCESS, null);
//    }
}
