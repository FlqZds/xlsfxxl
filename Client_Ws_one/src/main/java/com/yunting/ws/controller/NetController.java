package com.yunting.ws.controller;

import com.alibaba.fastjson2.JSON;
import com.yunting.common.results.ResponseEnum;
import com.yunting.common.results.ResultMessage;
import com.yunting.ws.entity.incondition;
import com.yunting.ws.service.AdminService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/net")
public class NetController {
    @Resource(name = "AdminService")
    private AdminService adminService;

    // 接收用户状态的更改
    @PostMapping("/operateUser")
    public ResultMessage changeUserPower(@RequestBody String condition) throws IOException {
        incondition to = JSON.to(incondition.class, condition);
        ResultMessage msg = adminService.changeUserPower(to);
        return msg;
    }

}
