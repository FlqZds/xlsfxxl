package com.yunting.client.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class incondition {
    private  String condition;
    private  String operationMsg;
    private  String appid;
    private String playerId;
    private String date;
    private Integer page=1;
}
