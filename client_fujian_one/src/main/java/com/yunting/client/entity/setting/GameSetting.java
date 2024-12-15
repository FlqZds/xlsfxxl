package com.yunting.client.entity.setting;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "游戏设置")
public class GameSetting {
    private Integer appSettingId;
    @ApiModelProperty(value = "包名", dataType = "String")
    private String packageName;

    @ApiModelProperty(value = "应用名称", dataType = "String")
    private String appName;

    @ApiModelProperty(value = "用户奖励比例设置的id", dataType = "Integer")
    private Integer userRewardSettingId;

    @ApiModelProperty(value = "用户聚集设置的id", dataType = "String")
    private Integer userGatheringSettingId;

    @ApiModelProperty(value = "截图设置的id", dataType = "long")
    private Integer screenshotSettingId;
    //           其他设置               其他设置             其他设置          其他设置
    @ApiModelProperty(value = "mac地址允许多少位用户", dataType = "String")
    private String macAddressSetting;

    @ApiModelProperty(value = "设备系统低于以下禁止登录", dataType = "Integer")
    private Integer deviceSystemCondition;

    @ApiModelProperty(value = "留存的记录方式", dataType = "String")
    private String retainWay;

    @ApiModelProperty(value = "点击量请求限制记录", dataType = "String")
    private Integer clickLimit;

    @ApiModelProperty(value = "禁止观看广告时间的时间段，起始时间,类型:Character", dataType = "Character")
    private String prohibtSeeAdvStart;

    @ApiModelProperty(value = "禁止观看广告时间的时间段，终止时间,类型:Character", dataType = "Character")
    private String prohibtSeeAdvEnd;

    @ApiModelProperty(value = "是否启用功能 ：禁止观看广告时间段", dataType = "String")
    private String isEnableProhibt;

    @ApiModelProperty(value = "每日活跃标准", dataType = "String")
    private String activeStandard;

    @ApiModelProperty(value = "请求广告与收下奖励时间间隔", dataType = "String")
    private String advWatchInterval;

    @ApiModelProperty(value = "通知公告", dataType = "String")
    private String noticeMSG;


}