package com.yunting.sum.util;

import com.yunting.common.Dto.RiskControlSetting;
import com.yunting.common.utils.RedisUtil_Record;
import com.yunting.common.utils.ST;
import com.yunting.common.utils.SpringRollBackUtil;
import com.yunting.sum.entity.SumCountNewplayer;
import com.yunting.sum.entity.SumProxy;
import com.yunting.sum.entity.setting.*;
import com.yunting.sum.entity.total_data.TotalAdn;
import com.yunting.sum.entity.total_data.TotalAdvlegends;
import com.yunting.sum.entity.total_data.TotalEcpm;
import com.yunting.sum.entity.total_data.TotalProfile;
import com.yunting.sum.mapper.AdnMapper;
import com.yunting.sum.mapper.SettingUpdateMapper;
import com.yunting.sum.mapper.SumMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static com.yunting.common.utils.FS.*;

@Component("ExecuteTask")
@Data
@AllArgsConstructor
@Builder
@Slf4j
public class ExecuteTask {

    @Resource(name = "RedisUtil_Record")
    private RedisUtil_Record rur;

    @Resource(name = "SumMapper")
    private SumMapper sumMapper;

    @Resource(name = "ST")
    private ST st;

    private static Integer day = 28;
    private static LocalDate whatDate = LocalDateTime.now().plusDays(-1).toLocalDate();//.withDayOfMonth(day)
    private static LocalDateTime startTime = LocalDateTime.now().plusDays(-1).withHour(0).withMinute(0).withSecond(0); //.withDayOfMonth(day)
    private static LocalDateTime endTime = LocalDateTime.now().plusDays(-1).withHour(23).withMinute(59).withSecond(59); //.withDayOfMonth(day)


    @Resource(name = "SettingUpdateMapper")
    private SettingUpdateMapper settingUpdateMapper;

    @Resource(name = "AdnMapper")
    private AdnMapper adnMapper;

    @Resource
    SqlSessionFactory sqlSessionFactory;

    public List<BigDecimal> nullToZero(BigDecimal... ecpmArgs) {
        ArrayList<BigDecimal> decimals = new ArrayList<>();
        for (BigDecimal ecpmArg : ecpmArgs) {
            if (ecpmArg == null) {
                ecpmArg = BigDecimal.ZERO;
            }
            decimals.add(ecpmArg);
        }
        return decimals;
    }


    // 任务_刷新游戏设置_清空次日redis留存_统计该日次日一级代理红包奖励_刷新在线用户表
    public void task_refresh_GameSetting_Daily() {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        Runnable runnable = new Runnable() {
            @Override
            @Transactional(rollbackFor = Exception.class)
            public void run() {
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
                String newNotice = settingUpdateMapper.getNewestNotification(st.PackageName());

                //奖励
                UserRewardSetting updatedRewardSetting = settingUpdateMapper.getUpdatedRewardSetting(st.PackageName());
                String rewardLimit = updatedRewardSetting.getGetRewardMaxVal().toString();
                String advPercentage = updatedRewardSetting.getUserAdvPercentage().toString();
                //聚集
                UserGatheringSetting updatedGatherSetting = settingUpdateMapper.getUpdatedGatherSetting(st.PackageName());
                boolean gathChoice = Objects.equals(updatedGatherSetting.getGatheringChoice().toString(), "0");
                String large = updatedGatherSetting.getGatheringPopulationLarge().toString();
                String small = updatedGatherSetting.getGatheringPopulationSmall().toString();
                String sameMacPopulation = updatedGatherSetting.getSameMacPopulation().toString();
                String deviceLimit = updatedGatherSetting.getGatheringDeviceLimit().toString();
                //提现
                WithdrawSetting withdrawSetting = settingUpdateMapper.getUpdatedWithdrawSetting(st.PackageName());
                String withdrawCount = withdrawSetting.getWithdrawCount().toString();
                String withdrawNojudgeMoney = withdrawSetting.getWithdrawNojudgeMoney().toString();
                String aSwitch = withdrawSetting.getCountSwitch();
                boolean countSwitch = Objects.equals(aSwitch, "0");
                String withdrawPercentage = withdrawSetting.getWithdrawPercentage();
                //截图
                ScreenshotSetting updatedScreenshotSetting = settingUpdateMapper.getUpdatedScreenshotSetting(st.GameId());
                String options = updatedScreenshotSetting.getScreenshotSettingOptions();
                boolean isShotSwitch = Objects.equals(options, "0");
                String codebitMaxVal = updatedScreenshotSetting.getScreenshotSettingVal().toString();
                String transLimitDaily = updatedScreenshotSetting.getTransLimitDaily().toString();
                String rewardCont = updatedScreenshotSetting.getTransRewardCont().toString();

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


                boolean retain_Way = Objects.equals(retainWay, "0");
                boolean is_Prohibt = Objects.equals(isEnableProhibt, "0");
                boolean is_Weekend = Objects.equals(isAnchorWeekend, "0");


                //其他
                rur.set("Daily_Active_Level", activeStandard);
                rur.set("Reset_Max_Time", resetMaxTime);
                rur.setBit("isIS_Weekend", Long.parseLong(st.GameId()), is_Weekend);

                rur.setBit("Forbid_Switch", Long.parseLong(st.GameId()), is_Prohibt);
                rur.set("Forbid_Begin_Time", prohibtSeeAdvStart);
                rur.set("Forbid_End_Time", prohibtSeeAdvEnd);

                rur.set("Min_System_Version", systemCondition);
                rur.set("ADV_Interval", advWatchInterval);
                rur.set("Notification", newNotice);

                log.info("当日其他设置已刷新");
                //奖励比例
                rur.set("ADV_Percent", advPercentage);
                rur.set("Reward_Limit", rewardLimit);
                log.info("当日奖励设置已刷新");

                //聚集
                rur.setBit("Gather_Choice", Long.parseLong(st.GameId()), gathChoice);
                rur.set("Gather_Big_Limit", large);
                rur.set("Gather_Small_Limit", small);
                rur.set("Same_Model_Limit", deviceLimit);
                rur.set("Same_Mac_Limit", sameMacPopulation);
                log.info("当日聚集设置已刷新");


                //截图
                rur.setBit("Shot_Switch", Long.parseLong(st.GameId()), isShotSwitch);
                rur.set("Codebit_Max_val", codebitMaxVal);
                rur.set("Daily_Max_Submit_Num", transLimitDaily);
                rur.set("Daily_Max_Watch_Num", rewardCont);
                log.info("当日截图设置已刷新");
                //提现设置
                rur.set("Withdraw_Nojudge_Money", withdrawNojudgeMoney);
                rur.set("Withdraw_Percentage", withdrawPercentage);
                rur.setBit("Daily_Withdraw_Switch", Long.parseLong(st.GameId()), countSwitch);
                rur.set("Daily_Withdraw_Count", withdrawCount);
                log.info("当日提现设置已刷新");
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
                log.info("当日风控设置已刷新");

                List<String> OneProxyName = adnMapper.getAllProxyName();//一级
                for (String proxyName : OneProxyName) {//是一级代理
                    BigDecimal myCash = adnMapper.getSumCashByProxyNameAndDate(proxyName, LocalDate.now()).getSumProxyCash();
                    List<String> allSon = adnMapper.getAllProxySonNameByProxyName(proxyName);
                    for (String son : allSon) {
                        SumProxy sonSum = adnMapper.getSumCashByProxyNameAndDate(son, LocalDate.now());
                        myCash.add(sonSum.getSumProxyCash());//记录旗下子代+自己的当日总佣金
                    }

                    sumMapper.modifyProxyCommissionCount(myCash, proxyName, LocalDate.now());
                    log.info(LocalDate.now() + proxyName + "_自己+子代当日红包奖励统计:" + myCash);
                }
            }
        };
        scheduledExecutorService.execute(runnable);

        scheduledExecutorService.shutdown();
        log.info("记载在线用户任务已关闭");
    }

    // 任务_刷新游戏设置_清空次日redis留存_统计该日次日一级代理红包奖励_刷新在线用户表
    public void task_summary_all() {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        Runnable runnable = new Runnable() {
            @Override
            @Transactional(rollbackFor = Exception.class)
            public void run() {
                Long gameID = Long.parseLong(st.GameId());
                List<String> OneProxyName = adnMapper.getAllProxyName();//一级
                List<String> sonName = adnMapper.getAllProxySonName();//二级
                List<String> allProxyName = new ArrayList<>();
                allProxyName.addAll(sonName);
                allProxyName.addAll(OneProxyName); //统计所有代理的数据

                //统计该应用的所有数据
                BigDecimal totalAdnOpen_gromore = BigDecimal.ZERO;
                BigDecimal totalAdnRow_gromore = BigDecimal.ZERO;
                BigDecimal totalEncourage_gromore = BigDecimal.ZERO;
                BigDecimal totalAdnWithdraw_gromore = BigDecimal.ZERO;
                BigDecimal totalAdnStream_gromore = BigDecimal.ZERO;
                BigDecimal totalAdnIn_gromore = BigDecimal.ZERO;
                BigDecimal totalAdnEcpm_gromore = BigDecimal.ZERO;

                BigDecimal totalAdnOpen_youlianghui = BigDecimal.ZERO;
                BigDecimal totalAdnRow_youlianghui = BigDecimal.ZERO;
                BigDecimal totalEncourage_youlianghui = BigDecimal.ZERO;
                BigDecimal totalAdnWithdraw_youlianghui = BigDecimal.ZERO;
                BigDecimal totalAdnStream_youlianghui = BigDecimal.ZERO;
                BigDecimal totalAdnIn_youlianghui = BigDecimal.ZERO;
                BigDecimal totalAdnEcpm_youlianghui = BigDecimal.ZERO;

                BigDecimal totalAdnOpen_kuaishou = BigDecimal.ZERO;
                BigDecimal totalAdnRow_kuaishou = BigDecimal.ZERO;
                BigDecimal totalEncourage_kuaishou = BigDecimal.ZERO;
                BigDecimal totalAdnWithdraw_kuaishou = BigDecimal.ZERO;
                BigDecimal totalAdnStream_kuaishou = BigDecimal.ZERO;
                BigDecimal totalAdnIn_kuaishou = BigDecimal.ZERO;
                BigDecimal totalAdnEcpm_kuaishou = BigDecimal.ZERO;

                BigDecimal totalAdnOpen_baidu = BigDecimal.ZERO;
                BigDecimal totalAdnRow_baidu = BigDecimal.ZERO;
                BigDecimal totalEncourage_baidu = BigDecimal.ZERO;
                BigDecimal totalAdnWithdraw_baidu = BigDecimal.ZERO;
                BigDecimal totalAdnStream_baidu = BigDecimal.ZERO;
                BigDecimal totalAdnIn_baidu = BigDecimal.ZERO;
                BigDecimal totalAdnEcpm_baidu = BigDecimal.ZERO;


//当日应用广告联盟总计
                BigDecimal all_baidu = BigDecimal.ZERO;
                BigDecimal all_youLiangHui = BigDecimal.ZERO;
                BigDecimal all_fastHand = BigDecimal.ZERO;
                BigDecimal all_gromore = BigDecimal.ZERO;
                BigDecimal all_total_adn = BigDecimal.ZERO;
//当日应用广告位总计
                BigDecimal allEcpm_in = BigDecimal.ZERO;
                BigDecimal allEcpm_stream = BigDecimal.ZERO;
                BigDecimal allEcpm_open = BigDecimal.ZERO;
                BigDecimal allEcpm_row = BigDecimal.ZERO;
                BigDecimal allEcpm_encourage = BigDecimal.ZERO;
                BigDecimal allEcpm_withdraw = BigDecimal.ZERO;
                BigDecimal allEcpm_total = BigDecimal.ZERO;

//当日用户总计
                BigDecimal allProfile_rebate = BigDecimal.ZERO;
                BigDecimal allProfile_charge_rebate = BigDecimal.ZERO;
                BigDecimal allProfile_judge_rebate = BigDecimal.ZERO;

                BigDecimal allProfile_red = BigDecimal.ZERO;
                BigDecimal allProfile_cash = BigDecimal.ZERO;
                BigDecimal allProfile_judge_cash = BigDecimal.ZERO;

                int allProfile_normal = 0;
                int allProfile_noCallback = 0;
                int allProfile_noReward = 0;

                SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);//创建一个具有批量处理功能的会话
                try {
                    for (String proxyName : allProxyName) {


                        String adnName = "穿山甲";
                        BigDecimal sumEcpm_rowStyle_Gromore = adnMapper.sumRowStyleEcpmCount(LocalDateTime.now(), LocalDateTime.now(), adnName, proxyName);
                        BigDecimal sumEcpm_encourage_withdraw_Gromore = adnMapper.sumEncourageEcpmCount(LocalDateTime.now(), LocalDateTime.now(), adnName, proxyName, "提现");
                        BigDecimal sumEcpm_encourage_Gromore = adnMapper.sumEncourageEcpmCount(LocalDateTime.now(), LocalDateTime.now(), adnName, proxyName, "激励");
                        BigDecimal sumEcpm_inscreen_Gromore = adnMapper.sumInscreenEcpmCount(startTime, endTime, adnName, proxyName);
                        BigDecimal sumEcpm_open_Gromore = adnMapper.sumOpenEcpmCount(startTime, endTime, adnName, proxyName);
                        BigDecimal sumEcpm_stream_Gromore = adnMapper.sumStreamEcpmCount(startTime, endTime, adnName, proxyName);
                        List<BigDecimal> decimals = nullToZero(sumEcpm_rowStyle_Gromore, sumEcpm_encourage_Gromore, sumEcpm_inscreen_Gromore, sumEcpm_open_Gromore, sumEcpm_stream_Gromore, sumEcpm_encourage_withdraw_Gromore);
                        sumEcpm_rowStyle_Gromore = decimals.get(0);
                        sumEcpm_encourage_Gromore = decimals.get(1);
                        sumEcpm_inscreen_Gromore = decimals.get(2);
                        sumEcpm_open_Gromore = decimals.get(3);
                        sumEcpm_stream_Gromore = decimals.get(4);
                        sumEcpm_encourage_withdraw_Gromore = decimals.get(5);

                        BigDecimal sumEcpm_total_Gromore = sumEcpm_rowStyle_Gromore.add(sumEcpm_encourage_Gromore).add(sumEcpm_inscreen_Gromore).add(sumEcpm_open_Gromore).add(sumEcpm_stream_Gromore);


                        TotalAdn adn_gromore_proxy = TotalAdn.builder().totalAdnDate(whatDate).totalAdnType(adnName)
                                .totalAdnOpen(sumEcpm_open_Gromore).totalAdnRow(sumEcpm_rowStyle_Gromore)
                                .totalEncourage(sumEcpm_encourage_Gromore)
                                .totalAdnWithdraw(sumEcpm_encourage_withdraw_Gromore)

                                .totalAdnStream(sumEcpm_stream_Gromore)
                                .totalAdnIn(sumEcpm_inscreen_Gromore)
                                .totalAdnEcpm(sumEcpm_total_Gromore)
                                .appid(gameID)
                                .proxyName(proxyName)
                                .build();

                        all_gromore = all_gromore.add(sumEcpm_total_Gromore);
                        totalAdnIn_gromore = totalAdnIn_gromore.add(sumEcpm_inscreen_Gromore);
                        totalAdnOpen_gromore = totalAdnOpen_gromore.add(sumEcpm_open_Gromore);
                        totalAdnRow_gromore = totalAdnRow_gromore.add(sumEcpm_rowStyle_Gromore);
                        totalEncourage_gromore = totalEncourage_gromore.add(sumEcpm_encourage_Gromore);
                        totalAdnWithdraw_gromore = totalAdnWithdraw_gromore.add(sumEcpm_encourage_withdraw_Gromore);
                        totalAdnStream_gromore = totalAdnStream_gromore.add(sumEcpm_stream_Gromore);
                        totalAdnEcpm_gromore = totalAdnEcpm_gromore.add(sumEcpm_total_Gromore);

                        log.info("日期:" + whatDate + "代理" + proxyName + "穿山甲插屏广告的该日总Ecpm:" + sumEcpm_inscreen_Gromore);
                        log.info("日期:" + whatDate + "代理" + proxyName + "穿山甲开屏广告的该日总Ecpm:" + sumEcpm_open_Gromore);
                        log.info("日期:" + whatDate + "代理" + proxyName + "穿山甲信息流广告的该日总Ecpm:" + sumEcpm_stream_Gromore);
                        log.info("日期:" + whatDate + "代理" + proxyName + "穿山甲普通激励广告的该日总Ecpm:" + sumEcpm_encourage_Gromore);
                        log.info("日期:" + whatDate + "代理" + proxyName + "穿山甲提现激励广告的该日总Ecpm:" + sumEcpm_encourage_withdraw_Gromore);
                        log.info("日期:" + whatDate + "代理" + proxyName + "穿山甲横幅广告的该日总Ecpm:" + sumEcpm_rowStyle_Gromore);

                        log.info("-----------------------------------------------------------------------------------");

                        adnName = "优量汇";
                        BigDecimal sumEcpm_rowStyle_YouLiangHui = adnMapper.sumRowStyleEcpmCount(LocalDateTime.now(), LocalDateTime.now(), adnName, proxyName);
                        BigDecimal sumEcpm_encourage_YouLiangHui = adnMapper.sumEncourageEcpmCount(LocalDateTime.now(), LocalDateTime.now(), adnName, proxyName, "激励");
                        BigDecimal sumEcpm_encourage_withdraw_YouLiangHui = adnMapper.sumEncourageEcpmCount(LocalDateTime.now(), LocalDateTime.now(), adnName, proxyName, "提现");
                        BigDecimal sumEcpm_inscreen_YouLiangHui = adnMapper.sumInscreenEcpmCount(startTime, endTime, adnName, proxyName);
                        BigDecimal sumEcpm_open_YouLiangHui = adnMapper.sumOpenEcpmCount(startTime, endTime, adnName, proxyName);
                        BigDecimal sumEcpm_stream_YouLiangHui = adnMapper.sumStreamEcpmCount(startTime, endTime, adnName, proxyName);
                        List<BigDecimal> bigDecimals = nullToZero(sumEcpm_rowStyle_YouLiangHui, sumEcpm_encourage_YouLiangHui, sumEcpm_inscreen_YouLiangHui, sumEcpm_open_YouLiangHui, sumEcpm_stream_YouLiangHui, sumEcpm_encourage_withdraw_YouLiangHui);
                        sumEcpm_rowStyle_YouLiangHui = bigDecimals.get(0);
                        sumEcpm_encourage_YouLiangHui = bigDecimals.get(1);
                        sumEcpm_inscreen_YouLiangHui = bigDecimals.get(2);
                        sumEcpm_open_YouLiangHui = bigDecimals.get(3);
                        sumEcpm_stream_YouLiangHui = bigDecimals.get(4);
                        sumEcpm_encourage_withdraw_YouLiangHui = bigDecimals.get(5);

                        BigDecimal sumEcpm_total_YouLiangHui = sumEcpm_rowStyle_YouLiangHui.add(sumEcpm_encourage_YouLiangHui).add(sumEcpm_inscreen_YouLiangHui).add(sumEcpm_open_YouLiangHui).add(sumEcpm_stream_YouLiangHui);

                        TotalAdn adn_youlianghui_proxy = TotalAdn.builder().totalAdnDate(whatDate).totalAdnType(adnName)
                                .totalAdnOpen(sumEcpm_open_YouLiangHui).totalAdnRow(sumEcpm_rowStyle_YouLiangHui)
                                .totalEncourage(sumEcpm_encourage_YouLiangHui)
                                .totalAdnWithdraw(sumEcpm_encourage_withdraw_YouLiangHui)

                                .totalAdnStream(sumEcpm_stream_YouLiangHui)
                                .totalAdnIn(sumEcpm_inscreen_YouLiangHui)
                                .totalAdnEcpm(sumEcpm_total_YouLiangHui)
                                .appid(gameID)
                                .proxyName(proxyName)
                                .build();

                        all_youLiangHui = all_youLiangHui.add(sumEcpm_total_YouLiangHui);
                        totalAdnIn_youlianghui = totalAdnIn_youlianghui.add(sumEcpm_inscreen_YouLiangHui);
                        totalAdnOpen_youlianghui = totalAdnOpen_youlianghui.add(sumEcpm_open_YouLiangHui);
                        totalAdnRow_youlianghui = totalAdnRow_youlianghui.add(sumEcpm_rowStyle_YouLiangHui);
                        totalEncourage_youlianghui = totalEncourage_youlianghui.add(sumEcpm_encourage_YouLiangHui);
                        totalAdnWithdraw_youlianghui = totalAdnWithdraw_youlianghui.add(sumEcpm_encourage_withdraw_YouLiangHui);
                        totalAdnStream_youlianghui = totalAdnStream_youlianghui.add(sumEcpm_stream_YouLiangHui);
                        totalAdnEcpm_youlianghui = totalAdnEcpm_youlianghui.add(sumEcpm_total_YouLiangHui);

                        log.info("日期:" + whatDate + "代理" + proxyName + "优量汇插屏广告的该日总Ecpm:" + sumEcpm_inscreen_YouLiangHui);
                        log.warn("日期:" + whatDate + "代理" + proxyName + "优量汇开屏广告的该日总Ecpm:" + sumEcpm_open_YouLiangHui);
                        log.info("日期:" + whatDate + "代理" + proxyName + "优量汇信息流广告的该日总Ecpm:" + sumEcpm_stream_YouLiangHui);
                        log.info("日期:" + whatDate + "代理" + proxyName + "优量汇激励广告的该日总Ecpm:" + sumEcpm_encourage_YouLiangHui);
                        log.info("日期:" + whatDate + "代理" + proxyName + "优量汇提现激励广告的该日总Ecpm:" + sumEcpm_encourage_withdraw_YouLiangHui);
                        log.info("日期:" + whatDate + "代理" + proxyName + "优量汇横幅广告的该日总Ecpm:" + sumEcpm_rowStyle_YouLiangHui);

                        log.info("-----------------------------------------------------------------------------------");


                        adnName = "百度";
                        BigDecimal sumEcpm_rowStyle_Baidu = adnMapper.sumRowStyleEcpmCount(LocalDateTime.now(), LocalDateTime.now(), adnName, proxyName);
                        BigDecimal sumEcpm_encourage_Baidu = adnMapper.sumEncourageEcpmCount(LocalDateTime.now(), LocalDateTime.now(), adnName, proxyName, "激励");
                        BigDecimal sumEcpm_encourage_withdraw_Baidu = adnMapper.sumEncourageEcpmCount(LocalDateTime.now(), LocalDateTime.now(), adnName, proxyName, "提现");
                        BigDecimal sumEcpm_inscreen_Baidu = adnMapper.sumInscreenEcpmCount(startTime, endTime, adnName, proxyName);
                        BigDecimal sumEcpm_open_Baidu = adnMapper.sumOpenEcpmCount(startTime, endTime, adnName, proxyName);
                        BigDecimal sumEcpm_stream_Baidu = adnMapper.sumStreamEcpmCount(startTime, endTime, adnName, proxyName);
                        List<BigDecimal> decimals1 = nullToZero(sumEcpm_rowStyle_Baidu, sumEcpm_encourage_Baidu, sumEcpm_inscreen_Baidu, sumEcpm_open_Baidu, sumEcpm_stream_Baidu, sumEcpm_encourage_withdraw_Baidu);
                        sumEcpm_rowStyle_Baidu = decimals1.get(0);
                        sumEcpm_encourage_Baidu = decimals1.get(1);
                        sumEcpm_inscreen_Baidu = decimals1.get(2);
                        sumEcpm_open_Baidu = decimals1.get(3);
                        sumEcpm_stream_Baidu = decimals1.get(4);
                        sumEcpm_encourage_withdraw_Baidu = decimals1.get(5);

                        BigDecimal sumEcpm_total_Baidu = sumEcpm_rowStyle_Baidu.add(sumEcpm_encourage_Baidu).add(sumEcpm_inscreen_Baidu).add(sumEcpm_open_Baidu).add(sumEcpm_stream_Baidu);

                        TotalAdn adn_baidu_proxy = TotalAdn.builder().totalAdnDate(whatDate).totalAdnType(adnName)
                                .totalAdnOpen(sumEcpm_open_Baidu).totalAdnRow(sumEcpm_rowStyle_Baidu)
                                .totalEncourage(sumEcpm_encourage_Baidu)
                                .totalAdnWithdraw(sumEcpm_encourage_withdraw_Baidu)

                                .totalAdnStream(sumEcpm_stream_Baidu)
                                .totalAdnIn(sumEcpm_inscreen_Baidu)
                                .totalAdnEcpm(sumEcpm_total_Baidu)
                                .appid(gameID)
                                .proxyName(proxyName)
                                .build();

                        all_baidu = all_baidu.add(sumEcpm_total_Baidu);
                        totalAdnIn_baidu = totalAdnIn_baidu.add(sumEcpm_inscreen_Baidu);
                        totalAdnOpen_baidu = totalAdnOpen_baidu.add(sumEcpm_open_Baidu);
                        totalAdnRow_baidu = totalAdnRow_baidu.add(sumEcpm_rowStyle_Baidu);
                        totalEncourage_baidu = totalEncourage_baidu.add(sumEcpm_encourage_Baidu);
                        totalAdnWithdraw_baidu = totalAdnWithdraw_baidu.add(sumEcpm_encourage_withdraw_Baidu);
                        totalAdnStream_baidu = totalAdnStream_baidu.add(sumEcpm_stream_Baidu);
                        totalAdnEcpm_baidu = totalAdnEcpm_baidu.add(sumEcpm_total_Baidu);


                        log.info("日期:" + whatDate + "代理" + proxyName + "百度插屏广告的该日总Ecpm:" + sumEcpm_inscreen_Baidu);
                        log.info("日期:" + whatDate + "代理" + proxyName + "百度开屏广告的该日总Ecpm:" + sumEcpm_open_Baidu);
                        log.info("日期:" + whatDate + "代理" + proxyName + "百度信息流广告的该日总Ecpm:" + sumEcpm_stream_Baidu);
                        log.info("日期:" + whatDate + "代理" + proxyName + "百度激励广告的该日总Ecpm:" + sumEcpm_encourage_Baidu);
                        log.info("日期:" + whatDate + "代理" + proxyName + "百度提现激励广告的该日总Ecpm:" + sumEcpm_encourage_withdraw_Baidu);
                        log.info("日期:" + whatDate + "代理" + proxyName + "百度横幅广告的该日总Ecpm:" + sumEcpm_rowStyle_Baidu);

                        log.info("-----------------------------------------------------------------------------------");


                        adnName = "快手";
                        BigDecimal sumEcpm_rowStyle_KuaiShou = adnMapper.sumRowStyleEcpmCount(LocalDateTime.now(), LocalDateTime.now(), adnName, proxyName);
                        BigDecimal sumEcpm_encourage_KuaiShou = adnMapper.sumEncourageEcpmCount(LocalDateTime.now(), LocalDateTime.now(), adnName, proxyName, "激励");
                        BigDecimal sumEcpm_encourage_withdraw_KuaiShou = adnMapper.sumEncourageEcpmCount(LocalDateTime.now(), LocalDateTime.now(), adnName, proxyName, "提现");
                        BigDecimal sumEcpm_inscreen_KuaiShou = adnMapper.sumInscreenEcpmCount(startTime, endTime, adnName, proxyName);
                        BigDecimal sumEcpm_open_KuaiShou = adnMapper.sumOpenEcpmCount(startTime, endTime, adnName, proxyName);
                        BigDecimal sumEcpm_stream_KuaiShou = adnMapper.sumStreamEcpmCount(startTime, endTime, adnName, proxyName);
                        List<BigDecimal> bigDecimals1 = nullToZero(sumEcpm_rowStyle_KuaiShou, sumEcpm_encourage_KuaiShou, sumEcpm_inscreen_KuaiShou, sumEcpm_open_KuaiShou, sumEcpm_stream_KuaiShou, sumEcpm_encourage_withdraw_KuaiShou);
                        sumEcpm_rowStyle_KuaiShou = bigDecimals1.get(0);
                        sumEcpm_encourage_KuaiShou = bigDecimals1.get(1);
                        sumEcpm_inscreen_KuaiShou = bigDecimals1.get(2);
                        sumEcpm_open_KuaiShou = bigDecimals1.get(3);
                        sumEcpm_stream_KuaiShou = bigDecimals1.get(4);
                        sumEcpm_encourage_withdraw_KuaiShou = bigDecimals1.get(5);

                        BigDecimal sumEcpm_total_KuaiShou = sumEcpm_rowStyle_KuaiShou.add(sumEcpm_encourage_KuaiShou).add(sumEcpm_inscreen_KuaiShou).add(sumEcpm_stream_KuaiShou).add(sumEcpm_open_KuaiShou);
                        TotalAdn adn_kuaiShou_proxy = TotalAdn.builder().totalAdnDate(whatDate).totalAdnType(adnName)
                                .totalAdnOpen(sumEcpm_open_KuaiShou).totalAdnRow(sumEcpm_rowStyle_KuaiShou)
                                .totalEncourage(sumEcpm_encourage_KuaiShou)
                                .totalAdnWithdraw(sumEcpm_encourage_withdraw_KuaiShou)

                                .totalAdnStream(sumEcpm_stream_KuaiShou)
                                .totalAdnIn(sumEcpm_inscreen_KuaiShou)
                                .totalAdnEcpm(sumEcpm_total_KuaiShou)
                                .appid(gameID)
                                .proxyName(proxyName)
                                .build();

                        all_fastHand = all_fastHand.add(sumEcpm_total_KuaiShou);
                        totalAdnIn_kuaishou = totalAdnIn_kuaishou.add(sumEcpm_inscreen_KuaiShou);
                        totalAdnOpen_kuaishou = totalAdnOpen_kuaishou.add(sumEcpm_inscreen_KuaiShou);
                        totalAdnRow_kuaishou = totalAdnRow_kuaishou.add(sumEcpm_rowStyle_KuaiShou);
                        totalEncourage_kuaishou = totalEncourage_kuaishou.add(sumEcpm_encourage_KuaiShou);
                        totalAdnWithdraw_kuaishou = totalAdnWithdraw_kuaishou.add(sumEcpm_encourage_withdraw_KuaiShou);
                        totalAdnStream_kuaishou = totalAdnStream_kuaishou.add(sumEcpm_stream_KuaiShou);
                        totalAdnEcpm_kuaishou = totalAdnEcpm_kuaishou.add(sumEcpm_total_KuaiShou);


                        all_total_adn = all_total_adn.add(all_fastHand).add(all_baidu).add(all_youLiangHui).add(all_gromore);
                        allEcpm_in = sumEcpm_inscreen_Baidu.add(sumEcpm_inscreen_KuaiShou).add(sumEcpm_inscreen_Gromore).add(sumEcpm_inscreen_YouLiangHui);
                        allEcpm_row = sumEcpm_rowStyle_Baidu.add(sumEcpm_rowStyle_KuaiShou).add(sumEcpm_rowStyle_Gromore).add(sumEcpm_rowStyle_YouLiangHui);
                        allEcpm_encourage = sumEcpm_encourage_KuaiShou.add(sumEcpm_encourage_Baidu).add(sumEcpm_encourage_withdraw_KuaiShou).add(sumEcpm_encourage_withdraw_Baidu);
                        allEcpm_stream = sumEcpm_stream_KuaiShou.add(sumEcpm_stream_Baidu).add(sumEcpm_stream_Gromore).add(sumEcpm_stream_YouLiangHui);
                        allEcpm_open = sumEcpm_open_KuaiShou.add(sumEcpm_open_Baidu).add(sumEcpm_open_Gromore).add(sumEcpm_open_YouLiangHui);
                        allEcpm_withdraw = sumEcpm_encourage_withdraw_KuaiShou.add(sumEcpm_encourage_withdraw_Baidu).add(sumEcpm_encourage_withdraw_Gromore).add(sumEcpm_encourage_withdraw_YouLiangHui);
                        allEcpm_total = allEcpm_in.add(allEcpm_row).add(allEcpm_encourage).add(allEcpm_stream).add(allEcpm_open).add(allEcpm_withdraw);


                        log.info("日期:" + whatDate + "代理" + proxyName + "快手插屏广告的该日总Ecpm:" + sumEcpm_inscreen_KuaiShou);
                        log.info("日期:" + whatDate + "代理" + proxyName + "快手开屏广告的该日总Ecpm:" + sumEcpm_open_KuaiShou);
                        log.info("日期:" + whatDate + "代理" + proxyName + "快手信息流广告的该日总Ecpm:" + sumEcpm_stream_KuaiShou);
                        log.info("日期:" + whatDate + "代理" + proxyName + "快手激励广告的该日总Ecpm:" + sumEcpm_encourage_KuaiShou);
                        log.info("日期:" + whatDate + "代理" + proxyName + "快手提现激励广告的该日总Ecpm:" + sumEcpm_encourage_withdraw_KuaiShou);
                        log.info("日期:" + whatDate + "代理" + proxyName + "快手横幅广告的该日总Ecpm:" + sumEcpm_rowStyle_KuaiShou);

                        //快手总ECPM
                        BigDecimal sumEcpm_KuaiShou_Total = sumEcpm_encourage_KuaiShou.add(sumEcpm_inscreen_KuaiShou).add(sumEcpm_open_KuaiShou).add(sumEcpm_stream_KuaiShou).add(sumEcpm_rowStyle_KuaiShou).add(sumEcpm_encourage_withdraw_KuaiShou);

                        //优量汇 总ECPM
                        BigDecimal sumEcpm_YouLiangHui_Total = sumEcpm_encourage_YouLiangHui.add(sumEcpm_inscreen_YouLiangHui).add(sumEcpm_open_YouLiangHui).add(sumEcpm_stream_YouLiangHui).add(sumEcpm_rowStyle_YouLiangHui).add(sumEcpm_encourage_withdraw_YouLiangHui);

                        //穿山甲 总ECPM
                        BigDecimal sumEcpm_Gromore_Total = sumEcpm_encourage_Gromore.add(sumEcpm_inscreen_Gromore).add(sumEcpm_open_Gromore).add(sumEcpm_stream_Gromore).add(sumEcpm_rowStyle_Gromore).add(sumEcpm_encourage_withdraw_Gromore);

                        //百度 总ECPM
                        BigDecimal sumEcpm_Baidu_Total = sumEcpm_encourage_Baidu.add(sumEcpm_inscreen_Baidu).add(sumEcpm_open_Baidu).add(sumEcpm_stream_Baidu).add(sumEcpm_rowStyle_Baidu).add(sumEcpm_encourage_withdraw_Baidu);

                        BigDecimal sumAdvLegends_Total = sumEcpm_KuaiShou_Total.add(sumEcpm_YouLiangHui_Total).add(sumEcpm_Gromore_Total).add(sumEcpm_Baidu_Total);

                        //广告联盟 统计
                        TotalAdvlegends advlegends_proxy = TotalAdvlegends.builder().totalDate(whatDate).totalEcpm(sumAdvLegends_Total)
                                .baidu(sumEcpm_Baidu_Total).fastHand(sumEcpm_KuaiShou_Total)
                                .gromore(sumEcpm_Gromore_Total).youLiangHui(sumEcpm_YouLiangHui_Total)
                                .appid(gameID)
                                .proxyName(proxyName)
                                .build();


                        //ECPM统计

                        //插屏广告
                        BigDecimal sumTotal_Ecpm_inscreen = sumEcpm_inscreen_Gromore.add(sumEcpm_inscreen_YouLiangHui).add(sumEcpm_inscreen_Baidu).add(sumEcpm_inscreen_KuaiShou);
                        //普通激励广告
                        BigDecimal sumTotal_Ecpm_encourage = sumEcpm_encourage_Gromore.add(sumEcpm_encourage_YouLiangHui).add(sumEcpm_encourage_Baidu).add(sumEcpm_encourage_KuaiShou);
                        //提现激励广告
                        BigDecimal sumTotal_Ecpm_encourage_withdraw = sumEcpm_encourage_withdraw_Gromore.add(sumEcpm_encourage_withdraw_YouLiangHui).add(sumEcpm_encourage_withdraw_Baidu).add(sumEcpm_encourage_withdraw_KuaiShou);
                        //信息流广告
                        BigDecimal sumTotal_Ecpm_stream = sumEcpm_stream_Gromore.add(sumEcpm_stream_YouLiangHui).add(sumEcpm_stream_Baidu).add(sumEcpm_stream_KuaiShou);
                        //开屏广告
                        BigDecimal sumTotal_Ecpm_open = sumEcpm_open_Gromore.add(sumEcpm_open_YouLiangHui).add(sumEcpm_open_Baidu).add(sumEcpm_open_KuaiShou);
                        //横幅广告
                        BigDecimal sumTotal_Ecpm_rowStyle = sumEcpm_rowStyle_Gromore.add(sumEcpm_rowStyle_YouLiangHui).add(sumEcpm_rowStyle_Baidu).add(sumEcpm_rowStyle_KuaiShou);
                        //总和
                        BigDecimal sumTotal_Ecpm_Total = sumTotal_Ecpm_inscreen.add(sumTotal_Ecpm_encourage).add(sumTotal_Ecpm_stream).add(sumTotal_Ecpm_open).add(sumTotal_Ecpm_rowStyle).add(sumTotal_Ecpm_encourage_withdraw);

                        TotalEcpm totalEcpm_proxy = TotalEcpm.builder().totalEcpmDate(whatDate).totalEcpm(sumTotal_Ecpm_Total)
                                .totalEcpmIn(sumTotal_Ecpm_inscreen)

                                .totalEcpmEncourage(sumTotal_Ecpm_encourage)
                                .withdrawEncourage(sumTotal_Ecpm_encourage_withdraw)

                                .totalEcpmStream(sumTotal_Ecpm_stream).totalEcpmOpen(sumTotal_Ecpm_open)
                                .totalEcpmRow(sumTotal_Ecpm_rowStyle)
                                .appid(gameID)
                                .proxyName(proxyName)
                                .build();

                        sqlSession.getConnection().setAutoCommit(false);
                        adnMapper.addTotalAdn(adn_baidu_proxy);               //该代理 百度    数据统计
                        adnMapper.addTotalAdn(adn_gromore_proxy);             //该代理 穿山甲   数据统计
                        adnMapper.addTotalAdn(adn_youlianghui_proxy);         //该代理 优量汇   数据统计
                        adnMapper.addTotalAdn(adn_kuaiShou_proxy);            //该代理 快手    数据统计

                        adnMapper.addTotalAdvLegends(advlegends_proxy); //该代理 广告联盟统计
                        adnMapper.addTotalEcpm(totalEcpm_proxy);        //该代理 ecpm统计


                        //该代理今日新增用户数量
                        //统计-对应的团队标识 今天所有新用户
                        Long thisDayNewPlayerList = sumMapper.getThisDayNewPlayer(proxyName, startTime, endTime, st.GameId());
                        log.info(proxyName + "今日新增用户数量:" + thisDayNewPlayerList);
                        SumCountNewplayer sumCountNewplayer = SumCountNewplayer.builder()
                                .newPlayerCount(thisDayNewPlayerList)
                                .proxyName(proxyName)
                                .appid(gameID)
                                .sumDay(LocalDate.now())
                                .build();
                        sumMapper.addNewPlayerCount(sumCountNewplayer);

                        BigDecimal csj_Cash = sumMapper.getThisDayEncourageProxyCommission(proxyName, ADN_csj, startTime, endTime);
                        BigDecimal bd_Cash = sumMapper.getThisDayEncourageProxyCommission(proxyName, ADN_bd, startTime, endTime);
                        BigDecimal ks_Cash = sumMapper.getThisDayEncourageProxyCommission(proxyName, ADN_ks, startTime, endTime);
                        BigDecimal ylh_Cash = sumMapper.getThisDayEncourageProxyCommission(proxyName, ADN_ylh, startTime, endTime);

                        SumProxy sumProxy_bd = SumProxy.builder()
                                .proxyName(proxyName)
                                .sumDay(LocalDate.now())
                                .adnName(ADN_bd)
                                .sumProxyCash(ylh_Cash)
                                .build();

                        SumProxy sumProxy_ks = SumProxy.builder()
                                .proxyName(proxyName)
                                .sumDay(LocalDate.now())
                                .adnName(ADN_ks)
                                .sumProxyCash(ylh_Cash)
                                .build();

                        SumProxy sumProxy_ylh = SumProxy.builder()
                                .proxyName(proxyName)
                                .sumDay(LocalDate.now())
                                .adnName(ADN_ylh)
                                .sumProxyCash(ylh_Cash)
                                .build();

                        SumProxy sumProxy_csj = SumProxy.builder()
                                .proxyName(proxyName)
                                .sumDay(LocalDate.now())
                                .adnName(ADN_csj)
                                .sumProxyCash(ylh_Cash)
                                .build();
                        sumMapper.addProxyCommissionCount(sumProxy_bd);
                        log.info(LocalDate.now() + proxyName + "_百度广告代理+" + proxyName + "当日红包奖励统计:" + bd_Cash);
                        sumMapper.addProxyCommissionCount(sumProxy_ylh);
                        log.info(LocalDate.now() + proxyName + "_优量汇广告代理+" + proxyName + "当日红包奖励统计:" + ylh_Cash);
                        sumMapper.addProxyCommissionCount(sumProxy_csj);
                        log.info(LocalDate.now() + proxyName + "_穿山甲广告代理+" + proxyName + "当日红包奖励统计:" + csj_Cash);
                        sumMapper.addProxyCommissionCount(sumProxy_ks);
                        log.info(LocalDate.now() + proxyName + "_快手广告代理+" + proxyName + "当日红包奖励统计:" + ks_Cash);


                        BigDecimal sumProxyCash = bd_Cash.add(ylh_Cash).add(csj_Cash).add(ks_Cash);
                        SumProxy sumProxy_all = SumProxy.builder()
                                .proxyName(proxyName)
                                .sumDay(LocalDate.now())
                                .adnName("all")
                                .sumProxyCash(sumProxyCash)
                                .build();
                        sumMapper.addProxyCommissionCount(sumProxy_all);
                        log.info(LocalDate.now() + proxyName + "_所有广告代理当日红包奖励统计:" + sumProxyCash);

                        //获取该代理的所有用户
                        List<String> myMember = adnMapper.getThisProxyAllPlayer(proxyName);
                        //通过该用户的留存记录统计该用户前一日的详细数据
                        for (String mbID : myMember) {
                            TotalProfile mbDayrecord = adnMapper.getDayBehaveRecordlist(mbID, startTime.toLocalDate());

                            if (mbDayrecord == null) {
                                log.info(mbID + "该用户今日留存数据为空");
                                continue;
                            } else {
                                allProfile_rebate = allProfile_rebate.add(mbDayrecord.getTotalCountDayRebate());
                                allProfile_charge_rebate = allProfile_charge_rebate.add(mbDayrecord.getTotalChargeRebate());
                                allProfile_judge_rebate = allProfile_judge_rebate.add(mbDayrecord.getTotalCountDayJudgeRebate());

                                allProfile_cash = allProfile_cash.add(mbDayrecord.getTotalCountDayCash());
                                allProfile_judge_cash = allProfile_judge_cash.add(mbDayrecord.getTotalCountDayJudgeCash());
                                allProfile_red = allProfile_red.add(mbDayrecord.getTotalRedDayUser());

                                allProfile_normal = mbDayrecord.getTotalCountDayNormal();
                                allProfile_noCallback = mbDayrecord.getTotalCountDayNoCallback();
                                allProfile_noReward = mbDayrecord.getTotalCountDayNoReward();
                            }
                        }
                        //若留存数据为空,或者用户为空,则该用户的所有数据都为空
                    }

                    TotalAdn all_baidu_adn = TotalAdn.builder()
                            .totalAdnDate(whatDate)
                            .appid(gameID)
                            .proxyName("")
                            .totalAdnEcpm(totalAdnEcpm_baidu)
                            .totalAdnIn(totalAdnIn_baidu)
                            .totalAdnRow(totalAdnRow_baidu)
                            .totalAdnOpen(totalAdnOpen_baidu)
                            .totalAdnStream(totalAdnStream_baidu)
                            .totalAdnWithdraw(totalAdnWithdraw_baidu)
                            .totalEncourage(totalEncourage_baidu)
                            .totalAdnType("百度")
                            .build();

                    TotalAdn all_gromore_adn = TotalAdn.builder()
                            .totalAdnDate(whatDate)
                            .appid(gameID)
                            .proxyName("")
                            .totalAdnEcpm(totalAdnEcpm_gromore)
                            .totalAdnIn(totalAdnIn_gromore)
                            .totalAdnRow(totalAdnRow_gromore)
                            .totalAdnOpen(totalAdnOpen_gromore)
                            .totalAdnStream(totalAdnStream_gromore)
                            .totalAdnWithdraw(totalAdnWithdraw_gromore)
                            .totalEncourage(totalEncourage_gromore)
                            .totalAdnType("穿山甲")
                            .build();

                    TotalAdn all_youLiangHui_adn = TotalAdn.builder()
                            .totalAdnDate(whatDate)
                            .appid(gameID)
                            .proxyName("")
                            .totalAdnEcpm(totalAdnEcpm_youlianghui)
                            .totalAdnIn(totalAdnIn_youlianghui)
                            .totalAdnRow(totalAdnRow_youlianghui)
                            .totalAdnOpen(totalAdnOpen_youlianghui)
                            .totalAdnStream(totalAdnStream_youlianghui)
                            .totalAdnWithdraw(totalAdnWithdraw_youlianghui)
                            .totalEncourage(totalEncourage_youlianghui)
                            .totalAdnType("优量汇")
                            .build();

                    TotalAdn all_fastHand_adn = TotalAdn.builder()
                            .totalAdnDate(whatDate)
                            .appid(gameID)
                            .proxyName("")
                            .totalAdnEcpm(totalAdnEcpm_kuaishou)
                            .totalAdnIn(totalAdnIn_kuaishou)
                            .totalAdnRow(totalAdnRow_kuaishou)
                            .totalAdnOpen(totalAdnOpen_kuaishou)
                            .totalAdnStream(totalAdnStream_kuaishou)
                            .totalAdnWithdraw(totalAdnWithdraw_kuaishou)
                            .totalEncourage(totalEncourage_kuaishou)
                            .totalAdnType("快手")
                            .build();


                    TotalAdvlegends all_totalAdvlegends = TotalAdvlegends.builder().totalDate(whatDate).appid(gameID).proxyName("")
                            .baidu(all_baidu)
                            .fastHand(all_fastHand)
                            .youLiangHui(all_youLiangHui)
                            .gromore(all_gromore)
                            .totalEcpm(all_total_adn)
                            .build();

                    TotalEcpm all_totalEcpm = TotalEcpm.builder().totalEcpmDate(whatDate).appid(gameID).proxyName("")
                            .totalEcpmIn(allEcpm_in)
                            .totalEcpmRow(allEcpm_row)
                            .totalEcpmOpen(allEcpm_open)
                            .totalEcpmEncourage(allEcpm_encourage)
                            .withdrawEncourage(allEcpm_withdraw)
                            .totalEcpmStream(allEcpm_stream)
                            .totalEcpm(allEcpm_total)
                            .build();

                    TotalProfile all_totalProfile = TotalProfile.builder().totalProfileDate(whatDate).appid(gameID).proxyName("")
                            .totalChargeRebate(allProfile_charge_rebate)
                            .totalCountDayRebate(allProfile_rebate)
                            .totalCountDayJudgeRebate(allProfile_judge_rebate)

                            .totalCountDayCash(allProfile_cash)
                            .totalCountDayJudgeCash(allProfile_judge_cash)
                            .totalRedDayUser(allProfile_red)

                            .totalCountDayNormal(allProfile_normal)
                            .totalCountDayNoCallback(allProfile_noCallback)
                            .totalCountDayNoReward(allProfile_noReward)
                            .build();

                    adnMapper.addTotalAdn(all_baidu_adn);               //该代理 百度    数据统计
                    adnMapper.addTotalAdn(all_gromore_adn);             //该代理 穿山甲   数据统计
                    adnMapper.addTotalAdn(all_youLiangHui_adn);         //该代理 优量汇   数据统计
                    adnMapper.addTotalAdn(all_fastHand_adn);            //该代理 快手    数据统计

                    adnMapper.addTotalProfile(all_totalProfile);        //所有用户的 明细汇总数据统计
                    adnMapper.addTotalAdvLegends(all_totalAdvlegends); //该代理 广告联盟统计
                    adnMapper.addTotalEcpm(all_totalEcpm);        //该代理 ecpm统计
                } catch (Exception e) {
                    log.error("代理数据统计失败" + e);
                    sqlSession.rollback();
                    SpringRollBackUtil.rollBack();
                } finally {
                    sqlSession.clearCache();
                    sqlSession.close();
                }

            }
        };

        scheduledExecutorService.execute(runnable);
        scheduledExecutorService.shutdown();
        log.info("记载在线用户任务已关闭");
    }
}
