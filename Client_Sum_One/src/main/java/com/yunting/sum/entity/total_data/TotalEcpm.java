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
public class TotalEcpm {

    private Long appid;


    private Integer totalEcpmId;

    private BigDecimal totalEcpmEncourage;

    private BigDecimal totalEcpmIn;

    private BigDecimal totalEcpmStream;

    private BigDecimal totalEcpmOpen;

    private BigDecimal totalEcpmRow;

    private BigDecimal withdrawEncourage;

    private BigDecimal totalEcpm;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate totalEcpmDate;
    private String proxyName;

}