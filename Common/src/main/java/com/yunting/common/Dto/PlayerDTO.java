package com.yunting.common.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component("PlayerDTO")
//@ApiModel(description = "玩家信息", value = "可以理解为传给客户端直接展示的一些数据")
public class PlayerDTO {

    //@ApiModelProperty(value = "玩家id", example = "1")
    private Long playerId;
    //@ApiModelProperty(value = "微信头像", example = "1")
    private String wxHeadimgurl;
    //@ApiModelProperty(value = "jwToken", example = "qrwhoifqwjreoiqwe")
    private String playerToken; //保证用户登录状态的token

    //@ApiModelProperty(value = "保证用户登录状态的token的有效期")
    private Long tokenExpiredTime; //保证用户登录状态的token的有效期
    //@ApiModelProperty(value = "游戏ID")
    private Long gameId;

}
