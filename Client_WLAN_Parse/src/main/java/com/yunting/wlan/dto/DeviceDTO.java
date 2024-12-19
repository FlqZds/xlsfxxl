package com.yunting.wlan.dto;

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
@ApiModel(value = "设备信息")
public class DeviceDTO {
    @ApiModelProperty("安卓id")
    private String androidID;

    @ApiModelProperty("oaid")
    private String oaid;

    @ApiModelProperty(value = "地址", dataType = "String")
    private String location;

    @ApiModelProperty(value = "该玩家mac", dataType = "String")
    private String thisMAC;

    @ApiModelProperty(value = "wifi列表", dataType = "List<String>")
    private String Wifi;


}
