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
public class TotalAdvlegends {

    private Integer totalAdvlegendsId;

    private BigDecimal totalEcpm;

    private BigDecimal baidu;

    private BigDecimal youLiangHui;

    private BigDecimal fastHand;

    private BigDecimal gromore;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate totalDate;
    private String proxyName;

    private Long appid;

}