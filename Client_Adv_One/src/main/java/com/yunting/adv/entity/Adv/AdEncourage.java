package com.yunting.adv.entity.Adv;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "激励广告这条记录")
public class AdEncourage {
    private Long advEncourageId;

    @ApiModelProperty(value = "广告类型id,Character类型(1开屏,2插屏,3信息流,4横幅,5激励)")
    private Character advTypeId = '5';

    @ApiModelProperty(value = "用户id")
    private Long playerId;

    @ApiModelProperty(value = "游戏id")
    private Long appID;

    @ApiModelProperty(value = "微信昵称")
    private String wxNickName ;

    @ApiModelProperty(value = "广告请求id")
    private String requestId ;

    @ApiModelProperty(value = "交易id")
    private String transId ;

    @ApiModelProperty(value = "代码位id")
    private String codeBitId ;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "被服务端回调时间", dataType = "LocalDateTime")
    private LocalDateTime serverCallbackTime;


    @ApiModelProperty(value = "是否被服务端回调", dataType = "Character")
    private Character isServerCall ;

    @ApiModelProperty(value = "是否被客户端回调", dataType = "Character")
    private Character isClientCall ;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "被客户端回调时间", dataType = "LocalDateTime")
    private LocalDateTime clientCallbackTime ;

    @ApiModelProperty(value = "请求时间", dataType = "LocalDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime requestTime;
    ;

    @ApiModelProperty(value = "该激励广告来源")
    private String encourageFrom;

    @ApiModelProperty(value = "广告奖励")
    private Double encourageReward;

    @ApiModelProperty(value = "看此激励广告可获取的ecpm")
    private Double encourageEcpm ;

    @ApiModelProperty(value = "看广告的第一次点击时间", dataType = "LocalDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime firstClickTime;
    ;
    @ApiModelProperty(value = "该次广告点击量")
    private Integer clickTimes ;

    @ApiModelProperty(value = "看广告点击按压时长(开红包)")
    private Integer askAdvPutTime ;

    @ApiModelProperty(value = "收下奖励点击按压时长")
    private Integer getRewardClickTimeDate ;

    @ApiModelProperty(value = "看此次广告的ip")
    private String ip;

    @ApiModelProperty(value = "是否关闭广告")
    private String isCloseEncourageAdv;

    @ApiModelProperty(value = "是否完整观看视频")
    private String isSeeEnd ;

    @ApiModelProperty(value = "是否展示广告")
    private String isDisplayAd ;

    @ApiModelProperty(value = "是否新老用户")
    private String isOldPlayer ;

    @ApiModelProperty(value = "错误信息")
    private String errInfo;

    @ApiModelProperty(value = "位置信息")
    private String address;

    @ApiModelProperty(value = "异常操作信息")
    private String exceptionOperate;

}