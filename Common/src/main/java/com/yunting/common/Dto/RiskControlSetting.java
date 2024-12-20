package com.yunting.common.Dto;

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
@ApiModel(description = "风控设置")
public class RiskControlSetting {
//    private Integer riskControllId;

    @ApiModelProperty(value = "包名", dataType = "String")
    private String packageName;

    @ApiModelProperty(value = "是否启用root，Character类型", dataType = "Character")
    private Character rootEnable;

    @ApiModelProperty(value = "是否启用无障碍  ，Character类型", dataType = "Character")
    private Character noBarrier;

    @ApiModelProperty(value = "是否启用usb，Character类型", dataType = "Character")
    private Character usb;

    @ApiModelProperty(value = "是否启用otg  ，Character类型", dataType = "Character")
    private Character otg;

    @ApiModelProperty(value = "是否启用 adb ，Character类型", dataType = "Character")
    private Character adb;

    @ApiModelProperty(value = "是否启用蓝牙，Character类型", dataType = "Character")
    private Character bluetooth;

    @ApiModelProperty(value = "是否启用热点，Character类型", dataType = "Character")
    private Character hotdot;

    @ApiModelProperty(value = "是否启用充电，Character类型", dataType = "Character")
    private Character charge;

    @ApiModelProperty(value = "是否启用vpn，Character类型", dataType = "Character")
    private Character vpn;

    @ApiModelProperty(value = "用户设备是否存在SIM卡，Character类型", dataType = "Character")
    private Character sim;

    @ApiModelProperty(value = "是否启用模拟器和云手机Enable，Character类型", dataType = "Character")
    private Character simulator;

    public RiskControlSetting(RiskControlSetting riskControlSetting) {
        this.rootEnable = riskControlSetting.rootEnable;
        this.noBarrier = riskControlSetting.noBarrier;
        this.usb = riskControlSetting.usb;
        this.otg = riskControlSetting.otg;
        this.adb = riskControlSetting.adb;
        this.bluetooth = riskControlSetting.bluetooth;
        this.hotdot = riskControlSetting.hotdot;
        this.charge = riskControlSetting.charge;
        this.vpn = riskControlSetting.vpn;
        this.sim = riskControlSetting.sim;
        this.simulator = riskControlSetting.simulator;
        this.packageName = riskControlSetting.packageName;
    }
}