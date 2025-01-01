package com.yunting.login.dto;

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
@ApiModel(value = "登录dto", description = "玩家没token登录需要携带的信息")
public class SignDto {

    @ApiModelProperty(value = "CPU", dataType = "String")
    private String mobileCpu;

    @ApiModelProperty(value = "CPU频率", dataType = "String")
    private String mobileCpuFluency;

    @ApiModelProperty(value = "设备系统", dataType = "String")
    private String mobileSystem;

    @ApiModelProperty("安卓id")
    private String androidID;

    @ApiModelProperty("oaid")
    private String oaid;

    @ApiModelProperty("手机品牌")
    private String deviceType;

    @ApiModelProperty("手机型号")
    private String deviceDetail;

    @ApiModelProperty("微信code")
    String wxCode;

    @ApiModelProperty("标识名称")
    String proxyName;

    @ApiModelProperty("地址")
    String location;

}
