package com.yunting.client.DTO.dataTransFer;

import com.yunting.client.DTO.VO.GameSettingVo;
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
@ApiModel(value = "玩家加入websocket后,要获取的游戏相关配置数据和玩家自身数据", description = "简称玩家元数据")
public class PlayerMetaData {

    @ApiModelProperty(value = "此时的游戏设置", dataType = "String")
    private GameSettingVo gameSettingVo;
    @ApiModelProperty(value = "此时的提现比例", dataType = "String")
    private String withdrawPercentage;
    @ApiModelProperty(value = "玩家支付宝id", dataType = "String")
    private String payLoginId;
    @ApiModelProperty(value = "玩家姓名", dataType = "String")
    private String realName;
    @ApiModelProperty(value = "玩家总累计红包值 (从注册到这次登录)", dataType = "String")
    private String totalRed;
    @ApiModelProperty(value = "玩家今日的累计红包总额", dataType = "String")
    private String todayRed;
    @ApiModelProperty(value = "玩家当前红包余额", dataType = "String")
    private String inRed;

    @ApiModelProperty(value = "代码位ecpm值大于")
    private Double screenshotSettingVal;

}
