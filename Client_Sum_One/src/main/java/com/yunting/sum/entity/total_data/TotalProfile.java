package com.yunting.sum.entity.total_data;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TotalProfile {

    private Long appid;

    private Integer totalProfileId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate totalProfileDate;


    private BigDecimal totalCountDayRebate; //当日返现打款总金额
    private BigDecimal totalCountDayJudgeRebate;  //当日返现审核总金额
    private BigDecimal totalChargeRebate; //当日充值返现总金额

    private BigDecimal totalRedDayUser;     //当日所有用户总余额
    private BigDecimal totalCountDayCash;  //当日提现打款 总金额
    private BigDecimal totalCountDayJudgeCash;  //当日提现审核 总金额

    private Integer totalCountDayNormal; //当日有服务端回调有奖励  数量
    private Integer totalCountDayNoReward;  //当日有服务端回调无奖励 总金额 (异常观看激励广告)
    private Integer totalCountDayNoCallback; //无服务端回调总数量

    private String proxyName;

}