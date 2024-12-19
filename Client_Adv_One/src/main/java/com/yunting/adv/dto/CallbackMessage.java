package com.yunting.adv.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "校验结果")
public class CallbackMessage {

    @ApiModelProperty(name = "判定是否发放奖励", example = "true")
    private boolean is_verify;
    private Integer reason;

}
