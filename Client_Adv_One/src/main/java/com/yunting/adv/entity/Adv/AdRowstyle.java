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
@ApiModel(description = "横幅广告这条记录")
public class AdRowstyle {
    private Long adRowstyleId;

    @ApiModelProperty(value = "广告类型id,Character类型(1开屏,2插屏,3信息流,4横幅,5激励)")
    private Character advTypeId='4';

    @ApiModelProperty(value = "用户id")
    private Long playerId=0L;

    @ApiModelProperty(value = "应用id")
    private String appId="";

    @ApiModelProperty(value = "代码位id")
    private String codeBitId="";

    @ApiModelProperty(value = "请求id")
    private String requestId="";

    @ApiModelProperty(value = "ecpm")
    private Double advEcpm=0.0;

    @ApiModelProperty(value = "点击次数")
    private Integer clicks=0;

    @ApiModelProperty(value = "ip")
    private String ip;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "看广告第一次点击的时间")
    private LocalDateTime firstClickTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "请求时间")
    private LocalDateTime requestTime;


    @ApiModelProperty(value = "用户昵称")
    private String wxNickName="";

    @ApiModelProperty(value = "错误信息")
    private String errinfo="";
}