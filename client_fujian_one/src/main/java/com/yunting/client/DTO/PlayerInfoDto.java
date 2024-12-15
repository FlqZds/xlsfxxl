package com.yunting.client.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayerInfoDto {
    @ApiModelProperty(value = "游戏ID", example = "1")
    private Long gameid;

    @ApiModelProperty(value = "用户ID", example = "小明")
    private Long playerId;

    @ApiModelProperty(value = "微信openID", example = "whrfi0qw423 ")
    private String wxOpenId;

    @ApiModelProperty(value = "真实姓名", example = "whrfi0qw423 ")
    private String realName;

    @ApiModelProperty(value = "微信昵称", example = "小明")
    private String wxNickname;

    @ApiModelProperty(value = "支付宝登录id", example = "whrfi0qw423 ")
    private String payLoginId;

    @ApiModelProperty(value = "所属团队", dataType = "Long.class")
    private Long proxyId;

    @ApiModelProperty(value = "红包余额", dataType = "BigDecimal.class")
    private BigDecimal redHad;

    @ApiModelProperty(value = "红包总奖励", dataType = "BigDecimal.class")
    private BigDecimal totalRed;

    @ApiModelProperty(value = "新老用户", dataType = "String.class")
    private String isNewIn;

    @ApiModelProperty(value = "留存", dataType = "String.class")
    private String retain;

    @ApiModelProperty(value = "是否活跃", dataType = "String.class")
    private String isActive;

    @ApiModelProperty(value = "AndroidID", dataType = "String.class")
    private String AndroidID;

    @ApiModelProperty(value = "OAID", dataType = "String.class")
    private String oaid;

    @ApiModelProperty(value = "手机品牌", dataType = "String.class")
    private String deviceType;

    @ApiModelProperty(value = "手机型号", dataType = "String.class")
    private String deviceDetail;

    @ApiModelProperty(value = "设备名称", dataType = "String")
    private String deviceName;

    @ApiModelProperty(value = "CPU", dataType = "String")
    private String mobileCpu;

    @ApiModelProperty(value = "CPU频率", dataType = "String")
    private String mobileCpuFluency;

    @ApiModelProperty(value = "设备系统", dataType = "String")
    private String mobileSystem;

    @ApiModelProperty(value = "封禁状态", dataType = "Character.class")
    private Character banStatus;

    @ApiModelProperty(value = "是否特殊用户", dataType = "Character.class")
    private String special;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //此注解用来接收字符串类型的参数封装成LocalDateTime类型
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8", shape = JsonFormat.Shape.STRING)
    //此注解将date类型数据转成字符串响应出去
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)        // 反序列化
    @JsonSerialize(using = LocalDateTimeSerializer.class)        // 序列化
    @ApiModelProperty(value = "玩家创建时间", dataType = "String.class")
    private LocalDateTime playerCreatTime;

}
