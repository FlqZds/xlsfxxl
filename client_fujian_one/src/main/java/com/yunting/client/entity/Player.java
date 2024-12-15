package com.yunting.client.entity;

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
public class Player {

    private Long playerId;

    private Long gameId;

    private LocalDateTime playerCreatTime;

    private String wxOpenId;

    private String realName;

    private String payLoginId;

    private Character status;

    private Long mobileId;

    private String wxNickname;

    private String wxHeadimgurl;

    private Long proxyId;

    private BigDecimal inRed;

    private String special;


}