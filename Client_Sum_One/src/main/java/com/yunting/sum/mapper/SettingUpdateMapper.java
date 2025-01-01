package com.yunting.sum.mapper;

import com.yunting.common.Dto.RiskControlSetting;
import com.yunting.sum.entity.setting.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component("SettingUpdateMapper")
public interface SettingUpdateMapper {
    //    查询更新后的游戏截图设置
    ScreenshotSetting getUpdatedScreenshotSetting(@Param("gameId") String gameId);

    //    查询更新后的游戏聚集设置
    UserGatheringSetting getUpdatedGatherSetting(@Param("packageName") String packageName);

    //    查询更新后的游戏奖励比例设置
    UserRewardSetting getUpdatedRewardSetting(@Param("packageName") String packageName);

    //    查询更新后的游戏提现设置
    WithdrawSetting getUpdatedWithdrawSetting(@Param("packageName") String packageName);

    //    查询更新后的游戏风控设置
    RiskControlSetting getUpdatedRiskSetting(@Param("packageName") String packageName);

    //    查询更新后的游戏其他设置
    GameSetting getUpdatedOtherSetting(@Param("packageName") String packageName);

    //    获取最新的通知
    String getNewestNotification(@Param("packageName") String packageName);

}