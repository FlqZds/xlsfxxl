package com.yunting.adv.dto;

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
@ApiModel(value = "广告记录数据")
public class AdDto {
    @ApiModelProperty(value = "代码位id")
    private String codeBitId = "";
    @ApiModelProperty(value = "请求id")
    private String requestId = "";
    @ApiModelProperty(value = "ecpm")
    private Double advEcpm = 0.0;
    @ApiModelProperty(value = "错误信息")
    private String errinfo = "";
}
