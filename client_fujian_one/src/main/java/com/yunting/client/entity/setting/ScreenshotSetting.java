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
@ApiModel(description = "截图设置")
public class ScreenshotSetting {
    private Integer screenshotSettingId;

    private Long gameId;

    @ApiModelProperty(value = "代码位ecpm值大于")
    private Double screenshotSettingVal;

    @ApiModelProperty(value = "当日允许用户提交截图订单上限")
    private Integer transLimitDaily;

    @ApiModelProperty(value = "j截图选项")
    private String screenshotSettingOptions;

    @ApiModelProperty(value = "领取奖励观看广告次数")
    private Integer transRewardCont;

}