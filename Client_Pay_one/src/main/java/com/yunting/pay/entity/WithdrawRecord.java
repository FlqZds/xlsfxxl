package com.yunting.pay.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
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
@ApiModel(description = "提现记录")
public class WithdrawRecord {

    private Long withdrawRecordId;

    @ApiModelProperty(value = "玩家id", dataType = "Long")
    private Long playerId;

    private String wxNickname;

    private String payLoginId;

    private String realName;

    private String packageName;

    private String withdrawFrom; //提现订单来源

    @ApiModelProperty(value = "提现状态", dataType = "Character")
    private Character withdrawStatus;

    @ApiModelProperty(value = "提现的金额", dataType = "BigDecimal")
    private BigDecimal withdrawMoney;

    @ApiModelProperty(value = "返现的金额", dataType = "BigDecimal")
    private BigDecimal returnMoney;

    private String withdrawPercentageNow;  //订单生成当时的提现比例

    @ApiModelProperty(value = "提现的时间", dataType = "LocalDateTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //此注解用来接收字符串类型的参数封装成LocalDateTime类型
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8", shape = JsonFormat.Shape.STRING)
    //此注解将date类型数据转成字符串响应出去
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)        // 反序列化
    @JsonSerialize(using = LocalDateTimeSerializer.class)        // 序列化
    private LocalDateTime withdrawTime;


}