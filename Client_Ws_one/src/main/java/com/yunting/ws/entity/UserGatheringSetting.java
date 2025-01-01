package com.yunting.ws.entity;

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
@ApiModel(description = "用户聚集限制设置")
public class UserGatheringSetting {
    private Integer userGatheringId;
    @ApiModelProperty(value = "聚集选择项", dataType = "Character")
    private Character gatheringChoice;
    @ApiModelProperty(value = "聚集大范围限制的人数", dataType = "Integer")
    private Integer gatheringPopulationLarge;
    @ApiModelProperty(value = "聚集小范围限制的人数", dataType = "Integer")
    private Integer gatheringPopulationSmall;
    @ApiModelProperty(value = "聚集手机同型号限制的设备数量", dataType = "Integer")
    private Integer gatheringDeviceLimit;
    @ApiModelProperty(value = "允许同一个mac地址在线用户数量,类型:Integer")
    private Integer sameMacPopulation;


}