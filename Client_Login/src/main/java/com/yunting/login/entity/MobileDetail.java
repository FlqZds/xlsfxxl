package com.yunting.login.entity;

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
@ApiModel(description = "设备详细信息")
public class MobileDetail {
    @ApiModelProperty(value = "设备ID", dataType = "Long")
    private Long mobileId;
    @ApiModelProperty(value = "设备名称", dataType = "String")
    private String deviceName;
    @ApiModelProperty(value = "手机品牌", dataType = "String")
    private String deviceType;
    @ApiModelProperty(value = "手机型号", dataType = "String")
    private String deviceDetail;
    @ApiModelProperty(value = "CPU", dataType = "String")
    private String mobileCpu;
    @ApiModelProperty(value = "CPU频率", dataType = "String")
    private String mobileCpuFluency;
    @ApiModelProperty(value = "设备系统", dataType = "String")
    private String mobileSystem;

}