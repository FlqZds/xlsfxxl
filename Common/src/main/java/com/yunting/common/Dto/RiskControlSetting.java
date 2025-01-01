package com.yunting.common.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@ApiModel(description = "风控设置")
public class RiskControlSetting {
//    private Integer riskControllId;

    //@ApiModelProperty(value = "包名", dataType = "String")
    private String packageName;

    //@ApiModelProperty(value = "是否启用root，String类型", dataType = "String")
    private String rootEnable;

    //@ApiModelProperty(value = "是否启用无障碍  ，String类型", dataType = "String")
    private String noBarrier;

    //@ApiModelProperty(value = "是否启用usb，String类型", dataType = "String")
    private String usb;

    //@ApiModelProperty(value = "是否启用otg  ，String类型", dataType = "String")
    private String otg;

    //@ApiModelProperty(value = "是否启用 adb ，String类型", dataType = "String")
    private String adb;

    //@ApiModelProperty(value = "是否启用蓝牙，String类型", dataType = "String")
    private String bluetooth;

    //@ApiModelProperty(value = "是否启用热点，String类型", dataType = "String")
    private String hotdot;

    //@ApiModelProperty(value = "是否启用充电，String类型", dataType = "String")
    private String charge;

    //@ApiModelProperty(value = "是否启用vpn，String类型", dataType = "String")
    private String vpn;

    //@ApiModelProperty(value = "用户设备是否存在SIM卡，String类型", dataType = "String")
    private String sim;

    //@ApiModelProperty(value = "是否启用模拟器和云手机Enable，String类型", dataType = "String")
    private String simulator;

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