package com.yunting.sum.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SumCountNewplayer {
    private Long sumId;

    private LocalDate sumDay;

    private Long newPlayerCount;

    private String proxyName;

    private Long appid;


}