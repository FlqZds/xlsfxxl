package com.yunting.client.DTO.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameSettingVo {
    @ApiModelProperty(value = "对应请求广告与收下奖励时间间隔", dataType = "String")
    private String advWatchInterval;

    @ApiModelProperty(value = "mac地址允许多少位用户", dataType = "String")
    private String macAddressSetting;

    @ApiModelProperty(value = "留存的记录方式", dataType = "String")
    private String retainWay;

    @ApiModelProperty(value = "点击量请求限制记录", dataType = "String")
    private String clickLimit;

    @ApiModelProperty(value = "禁止观看广告时间的时间段，起始时间,类型:Character", dataType = "Character")
    private String prohibtSeeAdvStart;

    @ApiModelProperty(value = "禁止观看广告时间的时间段，终止时间,类型:Character", dataType = "Character")
    private String prohibtSeeAdvEnd;

    @ApiModelProperty(value = "是否启用功能 ：禁止观看广告时间段", dataType = "String")
    private String isEnableProhibt;

    @ApiModelProperty(value = "用户在线观看广告间隔时长(小时)", dataType = "Character")
    private String seeAdvTimelong;

    @ApiModelProperty(value = "是否启用功能 ：观看广告间隔时长", dataType = "String")
    private String isEnableTimelong;

    @ApiModelProperty(value = "每日活跃标准", dataType = "String")
    private String activeStandard;

    @ApiModelProperty(value = "通知公告", dataType = "String")
    private String noticeMSG;
}
