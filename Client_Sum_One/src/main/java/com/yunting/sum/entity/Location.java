package com.yunting.sum.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Location {

    private Long locateId;

    private Long playerId;

    private String ip;

    private String location;

    private LocalDateTime recordTime;



}