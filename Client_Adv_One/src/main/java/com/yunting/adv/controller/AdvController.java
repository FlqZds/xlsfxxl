package com.yunting.adv.controller;

import com.google.gson.Gson;
import com.yunting.adv.dto.AdDto;
import com.yunting.adv.dto.AdEncourageDto;
import com.yunting.adv.dto.AdEncourageLoadDto;
import com.yunting.adv.dto.CallbackMessage;
import com.yunting.adv.services.RecordService;
import com.yunting.common.Dto.PlayerDTO;
import com.yunting.common.results.ResponseEnum;
import com.yunting.common.results.ResultMessage;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.util.HashMap;

@RestController
@RequestMapping("/Advertise")
@Api(tags = "广告记录接口")
@Slf4j
public class AdvController {

    @Resource(name = "RecordImpl")
    private RecordService recordService;


    @ApiOperation(value = "加载广告得到requestID上传广告记录")
    @PostMapping("/advEncourage/load")
    public ResultMessage loadAdEncourageRecord(@RequestBody AdEncourageLoadDto loadDto, @ApiIgnore @RequestAttribute("playerDTO") PlayerDTO playerDTO) {
        recordService.loadAndUpload(loadDto, playerDTO);

        return new ResultMessage(ResponseEnum.SUCCESS, null);
    }


    @ApiOperation(value = "观看激励广告上传记录")
    @PostMapping("/advEncourage/watch")
    public ResultMessage SaveAdEncourageRecord(@RequestBody AdEncourageDto adEncourageDto) {

        Long encourageId = recordService.watchAndUpload(adEncourageDto);

        return new ResultMessage(ResponseEnum.SUCCESS, encourageId);
    }


    @ApiOperation(value = "完成观看上传记录")
    @PutMapping("/advEncourage/isOk")
    public ResultMessage SaveAdEncourageRecord(@RequestParam("advEncourageId") Long advEncourageId) {

        recordService.isOKchangeAdEncourageRecord(advEncourageId);

        return new ResultMessage(ResponseEnum.SUCCESS, null);
    }


    @ApiOperation(value = "达到奖励条件上传记录")
    @PutMapping("/advEncourage/enough")
    public ResultMessage enoughToUpload(@ApiIgnore @RequestAttribute("playerDTO") PlayerDTO playerDTO, @RequestParam("advEncourageId") String advEncourageId, @ApiParam(value = "transID") @RequestParam("trasnId") String trasnId) {

        recordService.enoughchangeAdEncourageRecord(playerDTO, advEncourageId, trasnId);

        return new ResultMessage(ResponseEnum.SUCCESS, null);
    }


    @ApiOperation(value = "记录 看广告的点击量,首次点击时间,返回点击次数,也是long")
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功,", response = Long.class),
            @ApiResponse(code = 500, message = "请求失败"),
            @ApiResponse(code = 404, message = "未存在的资源请求"),
            @ApiResponse(code = 401, message = "未身份验证的请求"),
            @ApiResponse(code = 403, message = "未授权的请求"),

    })
    @PutMapping("/click")
    public ResultMessage SaveAdvClickCount(@RequestParam("advID") String advID) {

        log.info("click广告id:" + advID);
        Long clickResult = recordService.clickCountAndFirstClickTime(advID);

        return new ResultMessage(ResponseEnum.SUCCESS, clickResult);
    }

    @ApiOperation(value = "关闭广告 和其他四个基础广告的关闭广告共用,返回 红包值")
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功", response = BigDecimal.class),
            @ApiResponse(code = 88066, message = "提现广告已观看,请立马前往提现"),
            @ApiResponse(code = 500, message = "请求失败"),
            @ApiResponse(code = 404, message = "未存在的资源请求"),
            @ApiResponse(code = 401, message = "未身份验证的请求"),
            @ApiResponse(code = 403, message = "未授权的请求"),

    })
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "exceptionMsg", value = "违规内容", required = true),
                    @ApiImplicitParam(name = "clickCount", value = "点击次数", required = true),
                    @ApiImplicitParam(name = "packageName", value = "包名", required = true),
                    @ApiImplicitParam(name = "advID", value = "广告ID", required = true)
            })
    @PostMapping("/notice")
    public ResultMessage notifyNoClick(@ApiIgnore @RequestAttribute("playerDTO") PlayerDTO playerDTO, @ApiIgnore @RequestBody HashMap<String, String> requestMap) {

        String advID = requestMap.get("advID");
        String clickCount = requestMap.get("clickCount");
        String exceptionMsg = requestMap.get("exceptionMsg");

        BigDecimal redVal = recordService.closeRecordingClick(playerDTO, advID, Integer.valueOf(clickCount), exceptionMsg);
        return new ResultMessage(ResponseEnum.SUCCESS, redVal);
    }

    @ApiOperation(value = "补发点击量")
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功"),
            @ApiResponse(code = 500, message = "请求失败"),
            @ApiResponse(code = 55099, message = "点击量补发失败,详情请联系操作人员"),
            @ApiResponse(code = 404, message = "未存在的资源请求"),
            @ApiResponse(code = 401, message = "未身份验证的请求"),
            @ApiResponse(code = 403, message = "未授权的请求"),

    })
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "click", value = "要补发的点击量", required = true, paramType = "query"),
                    @ApiImplicitParam(name = "advID", value = "要补发的广告ID", required = true, paramType = "query")
            })
    @PutMapping("/advcpscl")
    public ResultMessage compensateClick(@RequestParam("advID") String advID, @RequestParam("click") Integer click) {

        recordService.compensateClick(advID, click);
        return new ResultMessage(ResponseEnum.SUCCESS, null);
    }

    @ApiOperation(value = "收下奖励,记录收下奖励按压时长")
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功", responseContainer = "红包值,该广告ecpm在当前提现配置计算后得出的红包值"),
            @ApiResponse(code = 500, message = "请求失败"),
            @ApiResponse(code = 404, message = "未存在的资源请求"),
            @ApiResponse(code = 401, message = "未身份验证的请求"),
            @ApiResponse(code = 403, message = "未授权的请求"),

    })
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "getWardTimeDate", value = "收下奖励时间", required = true, paramType = "query"),
                    @ApiImplicitParam(name = "advID", value = "广告ID", required = true, paramType = "query")
            })
    @PutMapping("/advEncourage/getWard")
    public ResultMessage closeEncourage(@RequestParam("advID") String advID, @RequestParam("getWardTimeDate") String getWardTimeDate) {

        recordService.getWardEncourage(advID, getWardTimeDate);
        return new ResultMessage(ResponseEnum.SUCCESS, null);
    }


    @ApiOperation(value = "保存插屏广告这条记录,返回对应广告id")
    @ApiImplicitParam(name = "adDto", value = "传输对象", required = true, paramType = "body")
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功,", response = Long.class),
            @ApiResponse(code = 500, message = "请求失败"),
            @ApiResponse(code = 404, message = "未存在的资源请求"),
            @ApiResponse(code = 401, message = "未身份验证的请求"),
            @ApiResponse(code = 403, message = "未授权的请求"),

    })
    @PostMapping("/advIn")
    public ResultMessage SaveAdInscreenRecord(HttpServletRequest request, @ApiIgnore @RequestAttribute("playerDTO") PlayerDTO playerDTO, @RequestBody AdDto adDto) {

        Long inscreenID = recordService.SaveAdInscreenRecord(request, playerDTO, adDto);

        return new ResultMessage(ResponseEnum.SUCCESS, inscreenID);
    }

    @ApiOperation(value = "保存开屏广告这条记录,返回对应广告id")
    @ApiImplicitParam(name = "adDto", value = "传输对象", required = true, paramType = "body")
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功,返回对应广告id", response = Long.class),
            @ApiResponse(code = 500, message = "请求失败"),
            @ApiResponse(code = 404, message = "未存在的资源请求"),
            @ApiResponse(code = 401, message = "未身份验证的请求"),
            @ApiResponse(code = 403, message = "未授权的请求"),

    })
    @PostMapping("/advOpen")
    public ResultMessage SaveAdOpenscreenRecord(HttpServletRequest request, @ApiIgnore @RequestAttribute("playerDTO") PlayerDTO playerDTO, @RequestBody AdDto adDto) {

        Long addopenscreenID = recordService.SaveAdOpenscreenRecord(request, playerDTO, adDto);

        return new ResultMessage(ResponseEnum.SUCCESS, addopenscreenID);
    }

    @ApiOperation(value = "保存信息流广告这条记录,返回对应广告id")
    @ApiImplicitParam(name = "adDto", value = "传输对象", required = true, paramType = "body")
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功,返回对应广告id", response = Long.class),
            @ApiResponse(code = 500, message = "请求失败"),
            @ApiResponse(code = 404, message = "未存在的资源请求"),
            @ApiResponse(code = 401, message = "未身份验证的请求"),
            @ApiResponse(code = 403, message = "未授权的请求"),

    })
    @PostMapping("/advStream")
    public ResultMessage SaveAdStreamRecord(HttpServletRequest request, @ApiIgnore @RequestAttribute("playerDTO") PlayerDTO playerDTO, @RequestBody AdDto adDto) {

        Long addstreamID = recordService.SaveAdStreamRecord(request, playerDTO, adDto);

        return new ResultMessage(ResponseEnum.SUCCESS, addstreamID);
    }

    @ApiOperation(value = "保存横幅广告这条记录,返回对应广告id")
    @ApiImplicitParam(name = "adDto", value = "传输对象", required = true, paramType = "body")
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功,返回对应广告id", response = Long.class),
            @ApiResponse(code = 500, message = "请求失败"),
            @ApiResponse(code = 404, message = "未存在的资源请求"),
            @ApiResponse(code = 401, message = "未身份验证的请求"),
            @ApiResponse(code = 403, message = "未授权的请求"),

    })
    @PostMapping("/advRow")
    public ResultMessage SaveRowStyleRecord(HttpServletRequest request, @ApiIgnore @RequestAttribute("playerDTO") PlayerDTO playerDTO, @RequestBody AdDto adDto) {

        Long add_rowStyle_result = recordService.SaveRowStyleRecord(request, playerDTO, adDto);

        return new ResultMessage(ResponseEnum.SUCCESS, add_rowStyle_result);
    }


    @ApiIgnore
    @GetMapping("/reward/callback")
    public String callBackIsReward(@RequestParam(value = "sign") String sign, @RequestParam(value = "user_id") String user_id, @RequestParam(value = "trans_id") String trans_id, @RequestParam(value = "reward_name") String reward_name, @RequestParam(value = "reward_amount") int reward_amount, @RequestParam(required = false) String extra) {
        CallbackMessage isReward = recordService.serverCallBackIsReward(sign, user_id, trans_id, reward_name, reward_amount, extra);
        String jsonString = new Gson().toJson(isReward);
        return jsonString;
    }


}
