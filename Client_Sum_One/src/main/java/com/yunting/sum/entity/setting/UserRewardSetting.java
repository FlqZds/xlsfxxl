package com.yunting.sum.entity.setting;

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
@ApiModel(description = "用户奖励比例设置")
public class UserRewardSetting {
//    private Integer userRewardSettingId;
@ApiModelProperty(value = "可领取奖励上限", dataType = "Integer")
    private Integer getRewardMaxVal;
@ApiModelProperty(value = "用户广告比例（%）", dataType = "Double")
    private Double userAdvPercentage;
}