package com.yunting.client.Controller;

import com.alibaba.fastjson2.JSON;
import com.github.pagehelper.PageInfo;
import com.yunting.adminservice.AdminService;
import com.yunting.client.DTO.RetainActive;
import com.yunting.client.DTO.incondition;
import com.yunting.common.results.ResponseEnum;
import com.yunting.common.results.ResultMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/Admin")
@Api(tags = "管理端接口")
@Slf4j
public class AdminController {

    @Resource(name = "AdminService")
    private AdminService adminService;

    @ApiOperation(value = "发给管理端 对应游戏id的 对应用户id的 某个每日啥啥啥")
    @GetMapping("/anyDetail")
    public ResultMessage sendPlayerAnyDetail(@RequestParam("appid") String appid, @RequestParam("playerId") Long playerId, @RequestParam("condition") String condition, @RequestParam("page") Integer page, @RequestParam("recordTime") String recordTime) {
        PageInfo pageInfo = adminService.queryPlayerAnyDetailByCondition(appid, playerId, condition, page, recordTime);
        return new ResultMessage(ResponseEnum.SUCCESS, pageInfo);
    }


    @ApiOperation(value = "发给管理端 对应条件的 操作记录")
    @PostMapping("/operation")
    public ResultMessage sendAllOperationRecord(@RequestBody String retainActive) {

        Map active_map = JSON.to(Map.class, retainActive);
//        这个类比较好用 ，就统一用它了
        RetainActive active = JSON.to(RetainActive.class, active_map.get("retainActive").toString());
        PageInfo pageInfo = adminService.queryAllOperation(active);
        return new ResultMessage(ResponseEnum.SUCCESS, pageInfo);
    }

    @ApiOperation(value = "发给管理端 对应条件的 设备信息")
    @PostMapping("/mobileInfo")
    public ResultMessage sendAllMobileInfo(@RequestBody String retainActive) {

        Map active_map = JSON.to(Map.class, retainActive);
//        这个类比较好用 ，就统一用它了
        RetainActive active = JSON.to(RetainActive.class, active_map.get("retainActive").toString());
        PageInfo pageInfo = adminService.queryAllMobileInfo(active);
        return new ResultMessage(ResponseEnum.SUCCESS, pageInfo);
    }


    @ApiOperation(value = "发给管理端 对应游戏id的 所有用户 留存活跃")
    @PostMapping("/retainActive")
    public ResultMessage sendAllUserRetainActive(@RequestBody String retainActive) {

        Map active_map = JSON.to(Map.class, retainActive);
        RetainActive active = JSON.to(RetainActive.class, active_map.get("retainActive").toString());

        PageInfo pageInfo = adminService.queryRetainActive(active);
        return new ResultMessage(ResponseEnum.SUCCESS, pageInfo);
    }


    @ApiOperation(value = "发给管理端 对应条件的 登陆异常")
    @PostMapping("/exception")
    public ResultMessage sendAllExceptionRecord(@RequestBody String retainActive) {

        Map active_map = JSON.to(Map.class, retainActive);
//        这个类比较好用 ，就统一用它了
        RetainActive active = JSON.to(RetainActive.class, active_map.get("retainActive").toString());
        PageInfo pageInfo = adminService.queryAllException(active);
        return new ResultMessage(ResponseEnum.SUCCESS, pageInfo);
    }


    @ApiOperation(value = "发给管理端 对应游戏id的 所有用户的信息")
    @PostMapping("/sendPlayerInfo")
    public ResultMessage sendPlayerInfo(@RequestBody RetainActive active) {
        PageInfo playerInfos = adminService.sendPlayerInfo(active);
        return new ResultMessage(ResponseEnum.SUCCESS, playerInfos);
    }


    // 接收提现记录的更改
    @PostMapping("/modifyCash")
    public ResultMessage changeWithdrawRecord(@RequestParam("packageName") String packageName) {
//        List<WithdrawRecord> withdrawRecords = adminService.queryAllWithdraw(packageName);

        return new ResultMessage(ResponseEnum.SUCCESS, null);
    }


    // 接收用户状态的更改
    @PostMapping("/operateUser")
    public ResultMessage changeUserPower(@RequestBody String condition) throws IOException {
        incondition to = JSON.to(incondition.class, condition);
        ResultMessage msg = adminService.changeUserPower(to);

        return msg;
    }


    // 接收用户状态的更改
    @GetMapping("/editMsg")
    public ResultMessage editMsg(@RequestParam("OperationMsg") String OperationMsg, @RequestParam("exceptionId") Long exceptionId, @RequestParam("type") Integer type) {

        adminService.modifyOptionMsg(OperationMsg, exceptionId, type);

        return new ResultMessage(ResponseEnum.SUCCESS, null);
    }


}
