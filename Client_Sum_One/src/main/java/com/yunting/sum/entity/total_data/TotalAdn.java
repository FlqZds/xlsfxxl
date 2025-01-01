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
public class TotalAdn {

    private Long appid;


    private Integer totalAdnId;

    private BigDecimal totalEncourage;

    private BigDecimal totalAdnOpen;

    private BigDecimal totalAdnIn;

    private BigDecimal totalAdnRow;

    private BigDecimal totalAdnStream;

    private BigDecimal totalAdnWithdraw;

    private BigDecimal totalAdnEcpm;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate totalAdnDate;

    private String totalAdnType;

    private String proxyName;

}