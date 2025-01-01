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

    private BigDecimal totalRedDayUser;

    private Integer totalCountDayRebate;

    private Integer totalCountDayJudgeRebate;

    private Integer totalCountDayCash;

    private Integer totalCountDayJudgeCash;

    private Integer totalCountDayNormal;

    private Integer totalCountDayNoReward;

    private Integer totalCountDayNoCallback;

    private BigDecimal totalChargeRebate;
    private String proxyName;

}