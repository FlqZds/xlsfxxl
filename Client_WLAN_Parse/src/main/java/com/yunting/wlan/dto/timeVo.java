package com.yunting.wlan.dto;

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
@ApiModel(value = "要传的信息")
public class timeVo {
    private int dayOfWeek; //这周星期几
    private Long beginTimeInterval;//时间(0点 到当前设置开始时间的时间戳[毫秒])
    private Long endTimeInterval;//时间(0点 到当前设置结束时间的时间戳[毫秒])
    private boolean forbidSwitch;  //禁止观看广告时间段的开关 1代表关闭,0代表开启
    private boolean enableWeekend;  //是否周末的开关 1代表关闭,0代表开启
}
