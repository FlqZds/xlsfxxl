package com.yunting.client.DTO;

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
@ApiModel(value = "激励广告记录数据")
public class AdEncourageDto {

    @ApiModelProperty(value = "激励广告记录ID")
    private Long advEncourageId;

    @ApiModelProperty(value = "代码位id")
    private String codeBitId="";

    @ApiModelProperty(value = "请求id")
    private String requestId="";

    @ApiModelProperty(value = "ecpm")
    private Double advEcpm=0.0;

    @ApiModelProperty(value = "错误信息")
    private String errinfo="";


}
