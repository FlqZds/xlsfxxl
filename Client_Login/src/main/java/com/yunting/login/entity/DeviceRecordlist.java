package com.yunting.login.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeviceRecordlist {
    @ApiModelProperty("设备id")
    private Long deviceId;
    @ApiModelProperty("玩家id")
    private Long playerId;
    @ApiModelProperty("昵称")
    private String wxNickname;
    @ApiModelProperty("oaid")
    private String oaid;
    @ApiModelProperty("androidId")
    private String androidId;
    @ApiModelProperty("手机品牌")
    private String deviceType;
    @ApiModelProperty("手机型号")
    private String deviceDetail;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "第一次使用该设备登录的时间", dataType = "LocalDateTime")
    private LocalDateTime firstlogintime;
    @ApiModelProperty(value = "该设备此次登录的身份", dataType = "LocalDateTime")
    private String identifiction;

}