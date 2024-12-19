package com.yunting.adv.entity;

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
public class DayBehaveRecordlist {
    private Long dayId;

    private String playerId;

    private String appId;

    private String retainWay;

    private BigDecimal todayred;

    private BigDecimal totalred;

    private Integer todayEncourageAdvCount;

    private Integer serviceCallBackAdvCount;

    private Integer serviceCallBackRewardCount;

    private LocalDateTime retainTime;

    private BigDecimal dayCash;


}