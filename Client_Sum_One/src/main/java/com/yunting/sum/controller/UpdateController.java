package com.yunting.sum.controller;

import com.alibaba.fastjson2.JSONObject;
import com.yunting.common.results.ResponseEnum;
import com.yunting.common.results.ResultMessage;
import com.yunting.sum.service.UpdateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.ResultSet;

@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("/sum")
public class UpdateController {
    @Resource(name = "UpdateService")
    private UpdateService updateService;

    //哪种游戏设置更新
    @PostMapping("upd")
    public ResultMessage updateOtherSetting(@RequestBody String type) {
        String s = JSONObject.parseObject(type).getString("type");
        updateService.updateOtherSetting(s);
        return new ResultMessage(ResponseEnum.SUCCESS, null);
    }
}
