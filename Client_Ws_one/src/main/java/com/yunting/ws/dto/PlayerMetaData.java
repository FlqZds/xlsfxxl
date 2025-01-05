package com.yunting.ws.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "玩家加入websocket后,要获取的游戏相关配置数据和玩家自身数据", description = "简称玩家元数据")
public class PlayerMetaData {

    @ApiModelProperty(value = "对应请求广告与收下奖励时间间隔", dataType = "int")
    private int advWatchInterval;

    @ApiModelProperty(value = "通知公告", dataType = "String")
    private String noticeMSG;

    @ApiModelProperty(value = "此时的提现比例", dataType = "String")
    private String withdrawPercentage;
    @ApiModelProperty(value = "玩家支付宝id", dataType = "String")
    private String payLoginId;
    @ApiModelProperty(value = "玩家姓名", dataType = "String")
    private String realName;
    @ApiModelProperty(value = "玩家总累计红包值 (从注册到这次登录)", dataType = "String")
    private String totalRed;
    @ApiModelProperty(value = "玩家今日的累计红包总额", dataType = "String")
    private String todayRed;
    @ApiModelProperty(value = "玩家当前红包余额", dataType = "String")
    private String inRed;

    @ApiModelProperty(value = "代码位ecpm值大于", dataType = "double")
    private Double screenshotSettingVal;
    @ApiModelProperty(value = "当日允许用户提交截图订单上限", dataType = "int")
    private Integer transLimitDaily;
    @ApiModelProperty(value = "截图选项")
    private String screenshotSettingOptions;
    @ApiModelProperty(value = "领取奖励观看广告次数", dataType = "int")
    private Integer transRewardCont;
    @ApiModelProperty(value = "后台最大闲置时长", dataType = "int")
    private Integer maxRestTime;


    @ApiModelProperty(value = "完成该任务总共能获得多少提现金额", dataType = "bigdecimal")
    private BigDecimal bonus;

    @ApiModelProperty(value = "当前看到第几个广告的进度", dataType = "int")
    private Integer taskProcess;

}
