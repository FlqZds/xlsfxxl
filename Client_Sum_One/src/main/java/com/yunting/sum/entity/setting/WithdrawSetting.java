package com.yunting.sum.entity.setting;

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
@ApiModel(description = "游戏提现配置")
public class WithdrawSetting {
    private Long withdrawSettingId;
    @ApiModelProperty(value = "包名", dataType = "Double")
    private String packageName;
    @ApiModelProperty(value = "提现比例", dataType = "Double")
    private String withdrawPercentage;
    @ApiModelProperty(value = "每日提现次数", dataType = "Double")
    private Integer withdrawCount;
    @ApiModelProperty(value = "每日提现次数的开关", dataType = "string")
    private String countSwitch;
    @ApiModelProperty(value = "用户免审核提现总金额", dataType = "Double")
    private BigDecimal withdrawNojudgeMoney;
}