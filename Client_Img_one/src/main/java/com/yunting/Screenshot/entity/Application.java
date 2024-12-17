package com.yunting.Screenshot.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "应用信息")
public class Application {

    private Long applicationId;

    private String packageName;
    @ApiModelProperty(value = "应用名称")
    private String applicationName;
    @ApiModelProperty(value = "游戏简介")
    private String applicationDesc;
    @ApiModelProperty("下载地址")
    private String downloadLocation;
    @ApiModelProperty("应用创建时间")
    private Date applicationCreateTime;

    private Boolean status;

    private String info;

    private String qqGroup;

    private String appid;

    private String appSecret;

}