package com.yunting.client.DTO.VO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RetainActiveVO {
    private String playerId;
    private String wxNickname;
    private String isRetain;
    private String isActive;
    private String retainTime;
    private Integer todayEncourageAdvCount;
}
