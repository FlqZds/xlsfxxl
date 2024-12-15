package com.yunting.client.DTO.VO;

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
@ApiModel(value = "要传的信息")
public class infoVO {
    @ApiModelProperty("安卓id")
    private String androidID;

    @ApiModelProperty("oaid")
    private String oaid;

    @ApiModelProperty("手机品牌")
    private String deviceType;

    @ApiModelProperty("手机型号")
    private String deviceDetail;

    @ApiModelProperty(value = "地址", dataType = "String")
    private String location;

    @ApiModelProperty(value = "CPU", dataType = "String")
    private String mobileCpu;

    @ApiModelProperty(value = "CPU频率", dataType = "String")
    private String mobileCpuFluency;

    @ApiModelProperty(value = "设备系统", dataType = "String")
    private String mobileSystem;

    @ApiModelProperty(value = "该玩家mac", dataType = "String")
    private String thisMAC;

    @ApiModelProperty(value = "wifi列表", dataType = "List<String>")
    private String Wifi;


}
