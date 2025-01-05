package com.yunting.ws.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OperationRecordlist {

    private Integer operationId;

    private String wxOpenId;

    private Long appid;

    private Long playerId;

    private String wxNickname;

    private String operationReason;

    private String operationType;

    private LocalDateTime operationTime;

}