package com.yunting.client.DTO.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameSettingVo {
    @ApiModelProperty(value = "对应请求广告与收下奖励时间间隔", dataType = "String")
    private String advWatchInterval;

    @ApiModelProperty(value = "通知公告", dataType = "String")
    private String noticeMSG;
}
