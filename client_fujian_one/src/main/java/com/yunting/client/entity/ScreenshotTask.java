package com.yunting.client.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScreenshotTask {
    private Integer taskId;

    private Integer taskCount;

    private LocalDate taskDate;

    private Long gameId;

    private Long playerId;

    private Integer taskProcess;

    private LocalDate taskEndTime;

    private BigDecimal bonus;

}