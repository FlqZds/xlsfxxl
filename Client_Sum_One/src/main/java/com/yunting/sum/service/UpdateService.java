package com.yunting.sum.service;


import com.yunting.common.Dto.PlayerDTO;
import com.yunting.common.Dto.RiskControlSetting;
import com.yunting.common.exception.AppException;
import com.yunting.common.results.ResponseEnum;
import com.yunting.common.utils.RedisUtil_Record;
import com.yunting.common.utils.ST;
import com.yunting.common.utils.TimeUtils;
import com.yunting.sum.entity.setting.*;
import com.yunting.sum.mapper.SettingUpdateMapper;
import com.yunting.sum.mapper.SumMapper;
import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.portable.ApplicationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service("UpdateService")
@Slf4j
public class UpdateService {

    @Resource(name = "SettingUpdateMapper")
    private SettingUpdateMapper settingUpdateMapper;

    @Resource(name = "ST")
    private ST st;

    @Resource(name = "RedisUtil_Record")
    private RedisUtil_Record rur;

    /***
     * 十二点发请求 记录在线用户
     * @param playerDTO
     * @param location  位置信息
     *
     */
    @Transactional(rollbackFor = Exception.class)
    public void refreshOnline(PlayerDTO playerDTO, String location) {
        Long playerId = playerDTO.getPlayerId();
        String isRetain = rur.get("No_Retain" + playerId);//是否有留存
        if (isRetain != null) {
            log.warn(playerId + "该用户今天已经在数据库有留存过");
            return;
        } else {
            sumMapper.updatePlayerDayrecord(playerId, LocalDateTime.now(), location, LocalDate.now());
            sumMapper.updateLocation(location, LocalDateTime.now(), playerId + "");
            rur.setEx("No_Retain" + playerId, location, TimeUtils.ONE_DAY_MILLISECONDS(), TimeUnit.SECONDS);
            log.info(playerDTO.getPlayerId() + "用户数据已留存");
        }
    }

    @Resource(name = "SumMapper")
    private SumMapper sumMapper;


    //在游戏设置变更之后,让缓存中的数据也进行更新
    public void updateOtherSetting(String type) {
        switch (type) {
            case "1": {//奖励比例
                UserRewardSetting updatedRewardSetting = settingUpdateMapper.getUpdatedRewardSetting(st.PackageName());
                String rewardLimit = updatedRewardSetting.getGetRewardMaxVal().toString();
                String advPercentage = updatedRewardSetting.getUserAdvPercentage().toString();

                rur.set("ADV_Percent", advPercentage);
                rur.set("Reward_Limit", rewardLimit);

                log.info("奖励设置已更新");
                break;
            }

            case "2": {//聚集
                UserGatheringSetting updatedGatherSetting = settingUpdateMapper.getUpdatedGatherSetting(st.PackageName());
                boolean gathChoice = Objects.equals(updatedGatherSetting.getGatheringChoice(), "0");
                String large = updatedGatherSetting.getGatheringPopulationLarge().toString();
                String small = updatedGatherSetting.getGatheringPopulationSmall().toString();
                String sameMacPopulation = updatedGatherSetting.getSameMacPopulation().toString();
                String deviceLimit = updatedGatherSetting.getGatheringDeviceLimit().toString();

                rur.setBit("Gather_Choice", Long.parseLong(st.GameId()), gathChoice);
                rur.set("Gather_Big_Limit", large);
                rur.set("Gather_Small_Limit", small);
                rur.set("Same_Model_Limit", deviceLimit);
                rur.set("Same_Mac_Limit", sameMacPopulation);

                log.info("聚集设置已更新");
                break;
            }

            case "3": {//截图
                ScreenshotSetting updatedScreenshotSetting = settingUpdateMapper.getUpdatedScreenshotSetting(st.GameId());
                String options = updatedScreenshotSetting.getScreenshotSettingOptions();
                String codebitMaxVal = updatedScreenshotSetting.getScreenshotSettingVal().toString();
                String transLimitDaily = updatedScreenshotSetting.getTransLimitDaily().toString();
                String rewardCont = updatedScreenshotSetting.getTransRewardCont().toString();

                boolean isShotSwitch = Objects.equals(options, "0");

                rur.setBit("Shot_Switch", Long.parseLong(st.GameId()), isShotSwitch);
                rur.set("Codebit_Max_val", codebitMaxVal);
                rur.set("Daily_Max_Submit_Num", transLimitDaily);
                rur.set("Daily_Max_Watch_Num", rewardCont);

                log.info("截图设置已更新");
                break;
            }

            case "4": {//其他
                log.info("当前包:" + st.PackageName());
                GameSetting updatedOtherSetting = settingUpdateMapper.getUpdatedOtherSetting(st.PackageName());
                String retainWay = updatedOtherSetting.getRetainWay();
                String resetMaxTime = updatedOtherSetting.getResetMaxTime().toString();
                String activeStandard = updatedOtherSetting.getActiveStandard();

                String isEnableProhibt = updatedOtherSetting.getIsEnableProhibt();
                String prohibtSeeAdvStart = updatedOtherSetting.getProhibtSeeAdvStart();
                String prohibtSeeAdvEnd = updatedOtherSetting.getProhibtSeeAdvEnd();
                String isAnchorWeekend = updatedOtherSetting.getIsAnchorWeekend();
                String advWatchInterval = updatedOtherSetting.getAdvWatchInterval();
                String systemCondition = updatedOtherSetting.getDeviceSystemCondition().toString();

                boolean retain_Way = Objects.equals(retainWay, "0");
                boolean is_Prohibt = Objects.equals(isEnableProhibt, "0");
                boolean is_Weekend = Objects.equals(isAnchorWeekend, "0");


                rur.setBit("Retain_Way", Long.parseLong(st.GameId()), retain_Way);
                rur.set("Daily_Active_Level", activeStandard);
                rur.set("Reset_Max_Time", resetMaxTime);
                rur.setBit("isIS_Weekend", Long.parseLong(st.GameId()), is_Weekend);

                rur.setBit("Forbid_Switch", Long.parseLong(st.GameId()), is_Prohibt);
                rur.set("Forbid_Begin_Time", prohibtSeeAdvStart);
                rur.set("Forbid_End_Time", prohibtSeeAdvEnd);

                rur.set("Min_System_Version", systemCondition);
                rur.set("ADV_Interval", advWatchInterval);
                log.info("其他设置已更新" + advWatchInterval);
                break;
            }

            case "5": {//通知公告
                String newNotice = settingUpdateMapper.getNewestNotification(st.PackageName());
                rur.set("Notification", newNotice);
                log.info("通知公告已变更");
                break;
            }

            case "6": {//风控设置
                RiskControlSetting risk = settingUpdateMapper.getUpdatedRiskSetting(st.PackageName());

                String bluetooth = risk.getBluetooth();
                String adb = risk.getAdb();
                String charge = risk.getCharge();
                String hotdot = risk.getHotdot();

                String otg = risk.getOtg();
                String root = risk.getRootEnable();
                String usb = risk.getUsb();
                String vpn = risk.getVpn();

                String noBarrier = risk.getNoBarrier();
                String sim = risk.getSim();
                String simulator = risk.getSimulator();

                //风控设置
                rur.hPut(st.GameId(), "bluetooth", bluetooth);
                rur.hPut(st.GameId(), "hotdot", hotdot);
                rur.hPut(st.GameId(), "charge", charge);
                rur.hPut(st.GameId(), "vpn", vpn);

                rur.hPut(st.GameId(), "sim", sim);
                rur.hPut(st.GameId(), "simulator", simulator);
                rur.hPut(st.GameId(), "root_enable", root);
                rur.hPut(st.GameId(), "no_barrier", noBarrier);

                rur.hPut(st.GameId(), "usb", usb);
                rur.hPut(st.GameId(), "otg", otg);
                rur.hPut(st.GameId(), "adb", adb);

                log.info("风控设置已更新");
                break;
            }

            case "7": {//提现设置
                WithdrawSetting withdrawSetting = settingUpdateMapper.getUpdatedWithdrawSetting(st.PackageName());
                String withdrawCount = withdrawSetting.getWithdrawCount().toString();
                String withdrawNojudgeMoney = withdrawSetting.getWithdrawNojudgeMoney().toString();
                String aSwitch = withdrawSetting.getCountSwitch();
                String withdrawPercentage = withdrawSetting.getWithdrawPercentage();
                boolean countSwitch = Objects.equals(aSwitch, "0");

                rur.set("Withdraw_Nojudge_Money", withdrawNojudgeMoney);
                rur.set("Withdraw_Percentage", withdrawPercentage);
                rur.setBit("Daily_Withdraw_Switch", Long.parseLong(st.GameId()), countSwitch);
                rur.set("Daily_Withdraw_Count", withdrawCount);

                log.info("提现设置已更新");
                break;
            }

        }
    }
}
