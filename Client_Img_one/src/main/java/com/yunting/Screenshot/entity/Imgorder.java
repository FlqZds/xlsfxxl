package com.yunting.Screenshot.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Imgorder {
    private String orderId;

    private Long orderPlayerId;

    private String androidID;

    private String wxname;

    private String packagename;

    private BigDecimal withdrawPercentage;

    private String orderImgGroup;

    private Long appid;

    private String isGet;

    @ApiModelProperty("商户名")
    private String orderBusiness;
    @ApiModelProperty("充值金额")
    private BigDecimal orderMoney;
    @ApiModelProperty(" 交易单号")
    private String orderTransId;
    @ApiModelProperty("商户单号")
    private String orderBusinessId;
    @ApiModelProperty("充值时间")
    private LocalDateTime orderPayTime;

    @ApiModelProperty("激励广告id")
    private String advEncourageId;
}