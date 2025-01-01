package com.yunting.sum.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.N;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SumProxy {
    private Long sumProxyId;

    private BigDecimal sumProxyCash;
    private BigDecimal sumProxyAllCash; //代理+子级代理的该日总红包奖励 一级代才有
    private BigDecimal sumProxyCommission; //该代理的佣金

    private LocalDate sumDay;

    private String proxyName;

    private String adnName;

}