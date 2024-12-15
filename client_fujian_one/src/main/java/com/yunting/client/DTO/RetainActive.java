package com.yunting.client.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RetainActive {
   private Long appid;
    private String        selectOption;
   private String search;
    private String        startTime;
   private String endTime;
       private Integer    page;
}
