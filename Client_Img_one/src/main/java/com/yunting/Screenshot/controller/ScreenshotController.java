package com.yunting.Screenshot.controller;

import com.yunting.Screenshot.dto.img.ImgContainer;
import com.yunting.Screenshot.dto.img.ImgShow;
import com.yunting.Screenshot.entity.Application;
import com.yunting.Screenshot.services.PlayerService;
import com.yunting.common.Dto.PlayerDTO;
import com.yunting.common.exception.AppException;
import com.yunting.common.results.ResponseEnum;
import com.yunting.common.results.ResultMessage;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/file")
@Slf4j
public class ScreenshotController {

    @Resource(name = "PlayerService")
    private PlayerService playerService;


    @ApiOperation(value = "获取文件名和hash值")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "packageName", value = "包名", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "imgType", value = "哪种截图", required = true, dataType = "String", paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功-SUCCESSFUL", response = ImgShow.class, responseContainer = "List"),
    })
    @GetMapping("/lstimg")
    public ResultMessage getPlayerScreenShot(@ApiIgnore @RequestAttribute("playerDTO") PlayerDTO playerDTO,
                                             @RequestParam String imgType) {
        List list = playerService.getThisPlayerPreUploadImg(playerDTO, imgType);
        log.info("查询图片类型" + imgType + "截图数据已返回");
        return new ResultMessage(ResponseEnum.SUCCESS, list);
    }


    @ApiOperation(value = "上传图片组")
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功-SUCCESSFUL"),
            @ApiResponse(code = 33070, message = "服务器异常,请检查相应服务器"),
            @ApiResponse(code = 33075, message = "数据不足无法完成该种请求操作,请检查上传对象数据是否完整或有丢失"),
            @ApiResponse(code = 33078, message = "错误的响应"),
            @ApiResponse(code = 66029, message = "无效或未知的响应"),
            @ApiResponse(code = 33063, message = "xml文件解析失败"),
            @ApiResponse(code = 33068, message = "特定加密算法在环境中不可用"),
            @ApiResponse(code = 33009, message = "无效的key,请检查key和授权令牌是否正确"),
            @ApiResponse(code = 33001, message = "服务内部发生错误或异常"),
            @ApiResponse(code = 41980, message = "上传文件数量过多,请分批次上传"),
            @ApiResponse(code = 41033, message = "数据量过大，请分批导入"),
            @ApiResponse(code = 41000, message = "数据为空，请重新导入"),
            @ApiResponse(code = 50978, message = "订单超时,上传图片失败"),
            @ApiResponse(code = 50575, message = "图片未找到或图片hash值不匹配,请使用正确的图片"),
            @ApiResponse(code = 50988, message = "截图中不包含订单截图,请检查上传图片"),
            @ApiResponse(code = 500, message = "发生未知异常，请联系管理员"),
    })
    @PostMapping("/uplf")
    public ResultMessage uploadFile(
            @ApiIgnore @RequestAttribute("playerDTO") PlayerDTO playerDTO,
            @RequestPart List<MultipartFile> files,
            @ApiParam(name = "安卓设备id") @RequestParam("aid") String androidID) throws IOException {
        if (Objects.isNull(files) || files.size() == 0) {
            log.error("空文件,请上传整正确的文件格式", new AppException(ResponseEnum.FILE_EMPTY_ERROR));
            return new ResultMessage(ResponseEnum.FILE_EMPTY_ERROR, null);
        }

        if (files.size() > 10) {
            log.error("上传文件数量过多,请分批次上传", new AppException(ResponseEnum.UPLOAD_FILE_TOO_MUCH));
            return new ResultMessage(ResponseEnum.UPLOAD_FILE_TOO_MUCH, null);
        }

        ResultMessage resultMessage = playerService.uploadImgByPlayerAndAddOrder(playerDTO, androidID, files);

        return resultMessage;
    }


    @ApiOperation(value = "上传文件名和哈希值")
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功-SUCCESSFUL"),
            @ApiResponse(code = 33070, message = "服务器异常,请检查相应服务器"),
            @ApiResponse(code = 33075, message = "数据不足无法完成该种请求操作,请检查上传对象数据是否完整或有丢失"),
            @ApiResponse(code = 33078, message = "错误的响应"),
            @ApiResponse(code = 66029, message = "无效或未知的响应"),
            @ApiResponse(code = 33063, message = "xml文件解析失败"),
            @ApiResponse(code = 33068, message = "特定加密算法在环境中不可用"),
            @ApiResponse(code = 33009, message = "无效的key,请检查key和授权令牌是否正确"),
            @ApiResponse(code = 33001, message = "服务内部发生错误或异常"),
            @ApiResponse(code = 41980, message = "上传文件数量过多,请分批次上传"),
            @ApiResponse(code = 419901, message = "图片已存在,请重新选择"),
            @ApiResponse(code = 500, message = "发生未知异常，请联系管理员"),
    })
    @PostMapping("/upnmhsh")
    public ResultMessage uploadFile(@ApiIgnore @RequestAttribute("playerDTO") PlayerDTO playerDTO,
                                    @ApiParam(name = "文件信息,其中的file字段可忽略")
                                    @RequestBody ImgContainer imgContainer) throws IOException {
        playerService.preUploadFileNameAndHashVal(playerDTO, imgContainer);
        return new ResultMessage(ResponseEnum.SUCCESS, null);
    }

    @ApiOperation(value = "更多娱乐,前端要把他自己给去掉")
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功-SUCCESSFUL", responseContainer = "List", response = Application.class),
            @ApiResponse(code = 80888, message = "更多游戏待上新...,敬请期待"),
            @ApiResponse(code = 500, message = "发生未知异常，请联系管理员")
    })
    @PutMapping("/rdtetmt")
    public ResultMessage manIdentify() {
        List moreEntertainment = playerService.getMoreEntertainment();
        return new ResultMessage(ResponseEnum.SUCCESS, moreEntertainment);
    }

    @ApiOperation(value = "立即下载")
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功-SUCCESSFUL"),
            @ApiResponse(code = 33070, message = "服务器异常,请检查相应服务器"),
            @ApiResponse(code = 33075, message = "数据不足无法完成该种请求操作,请检查上传对象数据是否完整或有丢失"),
            @ApiResponse(code = 33078, message = "错误的响应"),
            @ApiResponse(code = 66029, message = "无效或未知的响应"),
            @ApiResponse(code = 33063, message = "xml文件解析失败"),
            @ApiResponse(code = 33068, message = "特定加密算法在环境中不可用"),
            @ApiResponse(code = 33009, message = "无效的key,请检查key和授权令牌是否正确"),
            @ApiResponse(code = 33001, message = "服务内部发生错误或异常"),
            @ApiResponse(code = 500, message = "发生未知异常，请联系管理员"),
    })
    @PutMapping("/ckdwl")
    public ResultMessage clickDownload(@ApiIgnore @RequestAttribute("playerDTO") PlayerDTO playerDTO, @RequestBody String info) {
        playerService.download(playerDTO);
        return new ResultMessage(ResponseEnum.SUCCESS, null);
    }

}
