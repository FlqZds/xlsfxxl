package com.yunting.client.DTO.VO;

import com.yunting.client.entity.setting.RiskControlSetting;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameMeta {
    @ApiModelProperty(value = "玩家id", example = "1")
    private Long playerId;
    @ApiModelProperty(value = "游戏ID")
    private Long gameId;
    @ApiModelProperty(value = "是否为特殊用户",dataType = "Character")
    private String isSpecial;

    @ApiModelProperty(value = "微信头像", example = "1")
    private String wxHeadimgurl;
    @ApiModelProperty(value = "jwToken", example = "qrwhoifqwjreoiqwe")
    private String playerToken; //保证用户登录状态的token
    @ApiModelProperty(value = "保证用户登录状态的token的有效期")
    private Long tokenExpiredTime; //保证用户登录状态的token的有效期


    @ApiModelProperty("风控参数")
    private RiskControlSetting riskControlSetting;
    @ApiModelProperty("地址")
    private String address;
}
