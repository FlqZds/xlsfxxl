package com.yunting.client.mapper;

import com.yunting.client.entity.setting.GameSetting;
import com.yunting.client.entity.setting.RiskControlSetting;
import com.yunting.client.entity.setting.UserRewardSetting;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component("GameSettingMapper")
public interface GameSettingMapper {
    //    通过包名 获取游戏设置
    GameSetting getGameSettingByPackageName(@Param("packageName") String packageName);

    //主键获取该游戏的截图设置
    Double getScreenshotSettingByPk(Integer screenshotSettingID);

    UserRewardSetting getUserRewardSetting(@Param("userRewardSettingId") Integer userRewardSettingId);

    //包名获取风控参数
    RiskControlSetting getRiskControlSetting(@Param("packageName") String packageName);
}

