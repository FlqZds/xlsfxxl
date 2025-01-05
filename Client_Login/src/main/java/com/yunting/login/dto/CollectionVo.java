package com.yunting.login.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CollectionVo {
    @ApiModelProperty(value = "安装器", dataType = "String")
    private String installMachine;
    @ApiModelProperty(value = "应用市场", dataType = "String")
    private String appstore;
    private Long time;//时间(系统默认时间到当前时间的时间戳)
    private Long zeroTime;//时间(0点到当前时间的时间戳)
}
