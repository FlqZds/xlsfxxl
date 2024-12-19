package com.yunting.adv.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "激励广告加载广告数据")
public class AdEncourageLoadDto {
   @ApiModelProperty(value = "激励广告记录ID")
   private Long advEncourageId;
   @ApiModelProperty(value = "激励广告来源")
   private String encourageFrom;
   @ApiModelProperty(value = "激励广告请求时间")
   private String requestTime;
    @ApiModelProperty(value = "激励广告开红包按压时长")
    private Integer getRewardClickTimeDate;

}
