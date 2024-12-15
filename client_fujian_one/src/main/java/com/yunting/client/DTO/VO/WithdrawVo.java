package com.yunting.client.DTO.VO;

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
@ApiModel(description = "设备信息")
public class WithdrawVo {

    @ApiModelProperty(value = "玩家id", dataType = "Long")
    private Long playerID;

    @ApiModelProperty(value = "提现金额", dataType = "BigDecimal")
    private BigDecimal withdrawMoney;

    @ApiModelProperty(value = "返还金额", dataType = "BigDecimal")
    private BigDecimal returnMoney;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") //此注解用来接收字符串类型的参数封装成LocalDateTime类型
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8", shape = JsonFormat.Shape.STRING)
    //此注解将date类型数据转成字符串响应出去
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)        // 反序列化
    @JsonSerialize(using = LocalDateTimeSerializer.class)        // 序列化
    @ApiModelProperty(value = "提现时间", dataType = "LocalDateTime")
    private LocalDateTime withdrawTime;

    @ApiModelProperty(value = "提现状态", dataType = "String")
    private String withdrawStatus;

}
