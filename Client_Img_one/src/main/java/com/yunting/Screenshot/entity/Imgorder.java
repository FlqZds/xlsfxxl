package com.yunting.Screenshot.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Imgorder {
    private Long orderId;

    private BigDecimal orderMoney;

    private Long orderPlayerId;

    private String androidID;

    private String wxname;

    private String packagename;

    private String orderTransId;

    private BigDecimal withdrawPercentage;

    private String orderImgGroup;

    private Long appid;

    private String isGet;
}