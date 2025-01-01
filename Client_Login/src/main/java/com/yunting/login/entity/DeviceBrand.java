package com.yunting.login.entity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Api(description = "设备品牌信息")
public class DeviceBrand {
    @ApiModelProperty(value = "设备品牌", dataType = "String")
    private String brandName;
    @ApiModelProperty(value = "安装器", dataType = "String")
    private String installMachine;
    @ApiModelProperty(value = "应用市场", dataType = "String")
    private String appstore;
}
