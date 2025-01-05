package com.yunting.sum.controller;

import com.alibaba.fastjson2.JSONObject;
import com.yunting.common.Dto.PlayerDTO;
import com.yunting.common.results.ResponseEnum;
import com.yunting.common.results.ResultMessage;
import com.yunting.sum.entity.DeviceBrand;
import com.yunting.sum.entity.MobileDetail;
import com.yunting.sum.service.UpdateService;
import com.yunting.sum.util.ExecuteTask;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("/sum")
public class UpdateController {
    @Resource(name = "UpdateService")
    private UpdateService updateService;

    @ApiOperation(value = "十二点的时候发位置信息记录用户行为")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "location", value = "位置信息", required = true)
    })
    @PostMapping("/reOnline")
    public ResultMessage refreshOnline(@RequestBody String location, @ApiIgnore @RequestAttribute("playerDTO") PlayerDTO playerDTO) {
        updateService.refreshOnline(playerDTO, location);
        return new ResultMessage(ResponseEnum.SUCCESS, null);
    }


    //哪种游戏设置更新
    @PostMapping("upd")
    public ResultMessage updateOtherSetting(@RequestBody String type) {
        String s = JSONObject.parseObject(type).getString("type");
        updateService.updateOtherSetting(s);
        return new ResultMessage(ResponseEnum.SUCCESS, null);
    }

    @Resource(name = "ExecuteTask")
    private ExecuteTask executeTask;

}
