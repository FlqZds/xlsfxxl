package com.yunting.sum.service;

import com.yunting.common.Dto.RiskControlSetting;
import com.yunting.common.utils.ST;
import com.yunting.sum.entity.setting.*;
import com.yunting.sum.mapper.SettingUpdateMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Objects;

@Service("UpdateService")
@Slf4j
public class UpdateService {
    @Resource(name = "SettingUpdateMapper")
    private SettingUpdateMapper settingUpdateMapper;

    public void updateOtherSetting(String type) {
        switch (type) {
            case "1": {//奖励比例
                UserRewardSetting updatedRewardSetting = settingUpdateMapper.getUpdatedRewardSetting(ST.PackageName());
                Integer val = updatedRewardSetting.getGetRewardMaxVal();
                double advPercentage = updatedRewardSetting.getUserAdvPercentage();
                int percentage = (int) advPercentage;
                ST.changeReward(percentage, val);
                log.info("奖励设置已更新");
                break;
            }

            case "2": {//聚集
                UserGatheringSetting updatedGatherSetting = settingUpdateMapper.getUpdatedGatherSetting(ST.PackageName());
                boolean gathChoice = Objects.equals(updatedGatherSetting.getGatheringChoice(), "0");
                Integer large = updatedGatherSetting.getGatheringPopulationLarge();
                Integer small = updatedGatherSetting.getGatheringPopulationSmall();
                Integer sameMacPopulation = updatedGatherSetting.getSameMacPopulation();
                Integer deviceLimit = updatedGatherSetting.getGatheringDeviceLimit();
                ST.changeGath(gathChoice, large, small, sameMacPopulation, deviceLimit);
                log.info("聚集设置已更新");
                break;
            }

            case "3": {//截图
                ScreenshotSetting updatedScreenshotSetting = settingUpdateMapper.getUpdatedScreenshotSetting(ST.GameId());
                String options = updatedScreenshotSetting.getScreenshotSettingOptions();
                Double val = updatedScreenshotSetting.getScreenshotSettingVal();
                Integer transLimitDaily = updatedScreenshotSetting.getTransLimitDaily();
                Integer rewardCont = updatedScreenshotSetting.getTransRewardCont();

                if (Objects.equals(options, "1")) {
                    ST.changeScreenshot(false, val, transLimitDaily, rewardCont);
                }

                if (Objects.equals(options, "0")) {
                    ST.changeScreenshot(true, val, transLimitDaily, rewardCont);
                }
                log.info("截图设置已更新");
                break;
            }

            case "4": {//其他
                log.info("当前包:" + ST.PackageName());
                GameSetting updatedOtherSetting = settingUpdateMapper.getUpdatedOtherSetting(ST.PackageName());
                String retainWay = updatedOtherSetting.getRetainWay();
                Integer resetMaxTime = updatedOtherSetting.getResetMaxTime();
                String activeStandard = updatedOtherSetting.getActiveStandard();

                String isEnableProhibt = updatedOtherSetting.getIsEnableProhibt();
                String prohibtSeeAdvStart = updatedOtherSetting.getProhibtSeeAdvStart();
                String prohibtSeeAdvEnd = updatedOtherSetting.getProhibtSeeAdvEnd();
                String isAnchorWeekend = updatedOtherSetting.getIsAnchorWeekend();
                String advWatchInterval = updatedOtherSetting.getAdvWatchInterval();
                Integer systemCondition = updatedOtherSetting.getDeviceSystemCondition();

                boolean retain_Way = Objects.equals(retainWay, "0");
                int daily_Active_Level = Integer.parseInt(activeStandard);
                boolean is_Prohibt = Objects.equals(isEnableProhibt, "0");
                int startTime = Integer.parseInt(prohibtSeeAdvStart);
                int endTime = Integer.parseInt(prohibtSeeAdvEnd);
                boolean is_Weekend = Objects.equals(isAnchorWeekend, "0");
                int AdvWatchInterval = Integer.parseInt(advWatchInterval);


                ST.changeRedunt(retain_Way, daily_Active_Level, resetMaxTime, is_Weekend, is_Prohibt, startTime, endTime, AdvWatchInterval, systemCondition);

                log.info("其他设置已更新");
                break;
            }

            case "5": {//通知公告
                String newNotice = settingUpdateMapper.getNewestNotification(ST.PackageName());
                ST.changeNotification(newNotice);
                log.info("通知公告已变更");
                break;
            }

            case "6": {//风控设置
                RiskControlSetting risk = settingUpdateMapper.getUpdatedRiskSetting(ST.PackageName());
                ST.changeRisk(risk);
                log.info("风控设置已更新");
                break;
            }

            case "7": {//提现设置
                WithdrawSetting withdrawSetting = settingUpdateMapper.getUpdatedWithdrawSetting(ST.PackageName());
                Integer withdrawCount = withdrawSetting.getWithdrawCount();
                String withdrawNojudgeMoney = withdrawSetting.getWithdrawNojudgeMoney().toString();
                String aSwitch = withdrawSetting.getCountSwitch();
                String withdrawPercentage = withdrawSetting.getWithdrawPercentage();
                boolean countSwitch = Objects.equals(aSwitch, "0");
                ST.changeWithdraw(withdrawNojudgeMoney, withdrawPercentage, countSwitch, withdrawCount);
                log.info("提现设置已更新");
                break;
            }

        }
    }
}
