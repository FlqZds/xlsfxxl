package com.yunting.client.Controller;

import com.alibaba.fastjson2.JSON;
import com.yunting.clientservice.ClientImpl;
import com.yunting.common.Dto.PlayerDTO;
import com.yunting.common.results.ResponseEnum;
import com.yunting.common.results.ResultMessage;
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
public class WithdrawController {

    @Resource(name = "ClientService")
    private ClientImpl client;

    @ApiOperation(value = "提现申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "payId", value = "支付宝登录号", required = true),
            @ApiImplicitParam(name = "realName", value = "姓名", required = true),
            @ApiImplicitParam(name = "red", value = "提现金额", required = true)

    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "提现申请成功"),
            @ApiResponse(code = 67667, message = "用户未观看激励广告,请先去激励广告再来发起提现"),
            @ApiResponse(code = 66077, message = "免审核订单提现成功"),
            @ApiResponse(code = 66088, message = "订单已提交审核,请稍候"),
            @ApiResponse(code = 66666, message = "支付宝转账成功!!!"),
            @ApiResponse(code = 55000, message = "提现金额太小了"),
            @ApiResponse(code = 55001, message = "玩家已绑定支付宝,请先解绑"),
            @ApiResponse(code = 66006, message = "支付宝客户端初始化失败"),
            @ApiResponse(code = 55005, message = "支付宝转账接口调用失败,请联系管理员查看详情"),
            @ApiResponse(code = 55006, message = "您所提交的支付宝账号信息与上次有所不同"),
            @ApiResponse(code = 55090, message = "玩家提现次数已达上限,请明天再来吧"),
            @ApiResponse(code = 55001, message = "玩家已绑定支付宝,请先解绑"),
            @ApiResponse(code = 55888, message = "提现前请先去看一个广告")
    })
    @PostMapping("/cl/apy")
    public ResultMessage applyWithdraw(@ApiIgnore @RequestAttribute("playerDTO") PlayerDTO playerDTO, @ApiIgnore @RequestBody String info) {

        Long playerId = playerDTO.getPlayerId();

        HashMap<String, String> to = (HashMap<String, String>) JSON.to(Map.class, info);
        String payId = to.get("payId");
        String realName = to.get("realName");
        ResultMessage resultMessage = client.applyWithdraw(playerId, payId, realName);
        return resultMessage;
    }

    //    @ApiOperation(value = "绑定支付宝")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "payId", value = "支付宝登录号", required = true),
//            @ApiImplicitParam(name = "realName", value = "实名", required = true)
//    })
    @PostMapping("/cl/manfy")
    public ResultMessage manIdentify(@ApiIgnore @RequestAttribute("playerDTO") PlayerDTO playerDTO, @ApiIgnore @RequestBody String info) {
        Long playerId = playerDTO.getPlayerId();

        HashMap<String, String> to = (HashMap<String, String>) JSON.to(Map.class, info);
        String payId = to.get("payId");
        String realName = to.get("realName");
        Integer integer = client.manIdentify(playerId, payId, realName);
        if (integer != 1) {
            return new ResultMessage(ResponseEnum.MAN_IDENTIFY_FAILED, null);
        }
        log.info("支付宝绑定成功");
        return new ResultMessage(ResponseEnum.SUCCESS, null);
    }

}
