package com.yunting.sum.util;

import com.yunting.common.Dto.RiskControlSetting;
import com.yunting.common.exception.AppException;
import com.yunting.common.results.ResponseEnum;
import com.yunting.common.utils.RedisUtil_Record;
import com.yunting.common.utils.ST;
import com.yunting.common.utils.SpringRollBackUtil;
import com.yunting.sum.entity.DayBehaveRecordlist;
import com.yunting.sum.entity.SumCountNewplayer;
import com.yunting.sum.entity.SumProxy;
import com.yunting.sum.entity.setting.*;
import com.yunting.sum.entity.total_data.TotalAdn;
import com.yunting.sum.entity.total_data.TotalAdvlegends;
import com.yunting.sum.entity.total_data.TotalEcpm;
import com.yunting.sum.mapper.AdnMapper;
import com.yunting.sum.mapper.SettingUpdateMapper;
import com.yunting.sum.mapper.SumMapper;
import com.yunting.sum.service.DaySummaryCounter;
import io.swagger.annotations.ExampleProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

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
    private static LocalDate whatDate = LocalDateTime.now().toLocalDate();//.withDayOfMonth(day)
    private static LocalDateTime startTime = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0); //.withDayOfMonth(day)
    private static LocalDateTime endTime = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59); //.withDayOfMonth(day)


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


    // 任务_留存在线用户
    public void task_Save_onlineUser() {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        Runnable runnable = new Runnable() {
            @Override
            @Transactional(rollbackFor = Exception.class)
            public void run() {

                String way = st.Retain_Way() ? "1" : "0";

                SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);//创建一个具有批量处理功能的会话
                try {
                    sqlSession.getConnection().setAutoCommit(false);
                    for (int i = 1; i <= 500; i++) {

                        //将列表最后一个元素弹出,没有元素的话就抛出null
                        String inLine_user = rur.lRightPop("inLine_user");
                        Long size = rur.size("inLine_user");
                        if (inLine_user == null && size == 0) {
                            log.info("已经没有元素了,全部在线用户都已经存入数据库");
                            DaySummaryCounter.isSave = false;
                            scheduledExecutorService.shutdown();
                            return;
                        }

                        String[] s = inLine_user.split("_");
                        String playerID = s[0];
                        String location = s[1];

                        DayBehaveRecordlist dayBehaveRecord = DayBehaveRecordlist.builder()
                                .appId(st.APPID())
                                .playerId(playerID)
                                .retainWay(way)
                                .retainTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0)))

                                .dayCash(BigDecimal.ZERO)
                                .todayred(BigDecimal.ZERO)
                                .todayEncourageAdvCount(0)

                                .serviceCallBackAdvCount(0)
                                .serviceCallBackRewardCount(0)
                                .build();
                        sumMapper.addDayBehaveRecordlist(dayBehaveRecord);

                        sumMapper.updateLocation(location, LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0)), playerID);

                    }
                    sqlSession.commit(); //提交批量插入的数据
                } catch (Exception e) {
                    sqlSession.rollback(); //回滚数据
                    SpringRollBackUtil.rollBack();
                    log.error("批量添加在线用户数据出错,请联系管理员 . 错误信息:{}" + e.getMessage(), new AppException(ResponseEnum.RETAIN_BATCH_INLINE_FAILED));
                    throw new AppException(ResponseEnum.RETAIN_BATCH_INLINE_FAILED);
                } finally {
                    sqlSession.clearCache();
                    sqlSession.close();
                }

            }
        };
        if (DaySummaryCounter.isSave == true) {//执行 留存在线用户
            scheduledExecutorService.scheduleWithFixedDelay(runnable, 0, 10, TimeUnit.SECONDS);
        }

        if (DaySummaryCounter.isSave == false) { //杀掉留存的线程
            if (scheduledExecutorService.isShutdown() == false) {
                scheduledExecutorService.shutdown();
                log.info("记载在线用户任务已关闭");
            }
        }
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
                rur.setBit("Retain_Way", Long.parseLong(st.GameId()), retain_Way);
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

                //清除该日redis的用户留存记录
                rur.delete("retain_bitMap");
                rur.setBit("retain_bitMap", 0, true);

                rur.hPutIfAbsent("inLine_user", "0", ADN_bd);
                log.info(LocalDate.now() + "用户留存表已重置");
                log.info(LocalDate.now() + "在线用户表已新建");

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

                allProxyName.addAll(OneProxyName);
                for (String proxyName : allProxyName) {
                    String adnName = "穿山甲";
                    BigDecimal sumEcpm_rowStyle_Gromore = adnMapper.sumRowStyleEcpmCount(LocalDateTime.now(), LocalDateTime.now(), adnName, proxyName);
                    BigDecimal sumEcpm_encourage_Gromore = adnMapper.sumEncourageEcpmCount(LocalDateTime.now(), LocalDateTime.now(), adnName, proxyName);
                    BigDecimal sumEcpm_inscreen_Gromore = adnMapper.sumInscreenEcpmCount(startTime, endTime, adnName, proxyName);
                    BigDecimal sumEcpm_open_Gromore = adnMapper.sumOpenEcpmCount(startTime, endTime, adnName, proxyName);
                    BigDecimal sumEcpm_stream_Gromore = adnMapper.sumStreamEcpmCount(startTime, endTime, adnName, proxyName);
                    List<BigDecimal> decimals = nullToZero(sumEcpm_rowStyle_Gromore, sumEcpm_encourage_Gromore, sumEcpm_inscreen_Gromore, sumEcpm_open_Gromore, sumEcpm_stream_Gromore);
                    sumEcpm_rowStyle_Gromore = decimals.get(0);
                    sumEcpm_encourage_Gromore = decimals.get(1);
                    sumEcpm_inscreen_Gromore = decimals.get(2);
                    sumEcpm_open_Gromore = decimals.get(3);
                    sumEcpm_stream_Gromore = decimals.get(4);

                    BigDecimal sumEcpm_total_Gromore = sumEcpm_rowStyle_Gromore.add(sumEcpm_encourage_Gromore).add(sumEcpm_inscreen_Gromore).add(sumEcpm_open_Gromore).add(sumEcpm_stream_Gromore);
                    TotalAdn adn_gromore = TotalAdn.builder().totalAdnDate(whatDate).totalAdnType(adnName)
                            .totalAdnOpen(sumEcpm_open_Gromore).totalAdnRow(sumEcpm_rowStyle_Gromore)
                            .totalEncourage(sumEcpm_encourage_Gromore)
                            .totalAdnStream(sumEcpm_stream_Gromore)
                            .totalAdnIn(sumEcpm_inscreen_Gromore)
                            .totalAdnEcpm(sumEcpm_total_Gromore)
                            .appid(gameID)
                            .proxyName(proxyName)
                            .build();

                    log.info("日期:" + whatDate + "穿山甲插屏广告的该日总Ecpm:" + sumEcpm_inscreen_Gromore);
                    log.info("日期:" + whatDate + "穿山甲开屏广告的该日总Ecpm:" + sumEcpm_open_Gromore);
                    log.info("日期:" + whatDate + "穿山甲信息流广告的该日总Ecpm:" + sumEcpm_stream_Gromore);
                    log.info("日期:" + whatDate + "穿山甲激励广告的该日总Ecpm:" + sumEcpm_encourage_Gromore);
                    log.info("日期:" + whatDate + "穿山甲横幅广告的该日总Ecpm:" + sumEcpm_rowStyle_Gromore);

                    log.info("-----------------------------------------------------------------------------------");

                    adnName = "优量汇";
                    BigDecimal sumEcpm_rowStyle_YouLiangHui = adnMapper.sumRowStyleEcpmCount(LocalDateTime.now(), LocalDateTime.now(), adnName, proxyName);
                    BigDecimal sumEcpm_encourage_YouLiangHui = adnMapper.sumEncourageEcpmCount(LocalDateTime.now(), LocalDateTime.now(), adnName, proxyName);
                    BigDecimal sumEcpm_inscreen_YouLiangHui = adnMapper.sumInscreenEcpmCount(startTime, endTime, adnName, proxyName);
                    BigDecimal sumEcpm_open_YouLiangHui = adnMapper.sumOpenEcpmCount(startTime, endTime, adnName, proxyName);
                    BigDecimal sumEcpm_stream_YouLiangHui = adnMapper.sumStreamEcpmCount(startTime, endTime, adnName, proxyName);
                    List<BigDecimal> bigDecimals = nullToZero(sumEcpm_rowStyle_YouLiangHui, sumEcpm_encourage_YouLiangHui, sumEcpm_inscreen_YouLiangHui, sumEcpm_open_YouLiangHui, sumEcpm_stream_YouLiangHui);
                    sumEcpm_rowStyle_YouLiangHui = bigDecimals.get(0);
                    sumEcpm_encourage_YouLiangHui = bigDecimals.get(1);
                    sumEcpm_inscreen_YouLiangHui = bigDecimals.get(2);
                    sumEcpm_open_YouLiangHui = bigDecimals.get(3);
                    sumEcpm_stream_YouLiangHui = bigDecimals.get(4);

                    BigDecimal sumEcpm_total_YouLiangHui = sumEcpm_rowStyle_YouLiangHui.add(sumEcpm_encourage_YouLiangHui).add(sumEcpm_inscreen_YouLiangHui).add(sumEcpm_open_YouLiangHui).add(sumEcpm_stream_YouLiangHui);
                    TotalAdn adn_youlianghui = TotalAdn.builder().totalAdnDate(whatDate).totalAdnType(adnName)
                            .totalAdnOpen(sumEcpm_open_YouLiangHui).totalAdnRow(sumEcpm_rowStyle_YouLiangHui)
                            .totalEncourage(sumEcpm_encourage_YouLiangHui)
                            .totalAdnStream(sumEcpm_stream_YouLiangHui)
                            .totalAdnIn(sumEcpm_inscreen_YouLiangHui)
                            .totalAdnEcpm(sumEcpm_total_YouLiangHui)
                            .appid(gameID)
                            .proxyName(proxyName)
                            .build();

                    log.info("日期:" + whatDate + "优量汇插屏广告的该日总Ecpm:" + sumEcpm_inscreen_YouLiangHui);
                    log.warn("日期:" + whatDate + "优量汇开屏广告的该日总Ecpm:" + sumEcpm_open_YouLiangHui);
                    log.info("日期:" + whatDate + "优量汇信息流广告的该日总Ecpm:" + sumEcpm_stream_YouLiangHui);
                    log.info("日期:" + whatDate + "优量汇激励广告的该日总Ecpm:" + sumEcpm_encourage_YouLiangHui);
                    log.info("日期:" + whatDate + "优量汇横幅广告的该日总Ecpm:" + sumEcpm_rowStyle_YouLiangHui);

                    log.info("-----------------------------------------------------------------------------------");


                    adnName = "百度";
                    BigDecimal sumEcpm_rowStyle_Baidu = adnMapper.sumRowStyleEcpmCount(LocalDateTime.now(), LocalDateTime.now(), adnName, proxyName);
                    BigDecimal sumEcpm_encourage_Baidu = adnMapper.sumEncourageEcpmCount(LocalDateTime.now(), LocalDateTime.now(), adnName, proxyName);
                    BigDecimal sumEcpm_inscreen_Baidu = adnMapper.sumInscreenEcpmCount(startTime, endTime, adnName, proxyName);
                    BigDecimal sumEcpm_open_Baidu = adnMapper.sumOpenEcpmCount(startTime, endTime, adnName, proxyName);
                    BigDecimal sumEcpm_stream_Baidu = adnMapper.sumStreamEcpmCount(startTime, endTime, adnName, proxyName);
                    List<BigDecimal> decimals1 = nullToZero(sumEcpm_rowStyle_Baidu, sumEcpm_encourage_Baidu, sumEcpm_inscreen_Baidu, sumEcpm_open_Baidu, sumEcpm_stream_Baidu);
                    sumEcpm_rowStyle_Baidu = decimals1.get(0);
                    sumEcpm_encourage_Baidu = decimals1.get(1);
                    sumEcpm_inscreen_Baidu = decimals1.get(2);
                    sumEcpm_open_Baidu = decimals1.get(3);
                    sumEcpm_stream_Baidu = decimals1.get(4);

                    BigDecimal sumEcpm_total_Baidu = sumEcpm_rowStyle_Baidu.add(sumEcpm_encourage_Baidu).add(sumEcpm_inscreen_Baidu).add(sumEcpm_open_Baidu).add(sumEcpm_stream_Baidu);
                    TotalAdn adn_baidu = TotalAdn.builder().totalAdnDate(whatDate).totalAdnType(adnName)
                            .totalAdnOpen(sumEcpm_open_Baidu).totalAdnRow(sumEcpm_rowStyle_Baidu)
                            .totalEncourage(sumEcpm_encourage_Baidu)
                            .totalAdnStream(sumEcpm_stream_Baidu)
                            .totalAdnIn(sumEcpm_inscreen_Baidu)
                            .totalAdnEcpm(sumEcpm_total_Baidu)
                            .appid(gameID)
                            .proxyName(proxyName)
                            .build();

                    log.info("日期:" + whatDate + "百度插屏广告的该日总Ecpm:" + sumEcpm_inscreen_Baidu);
                    log.info("日期:" + whatDate + "百度开屏广告的该日总Ecpm:" + sumEcpm_open_Baidu);
                    log.info("日期:" + whatDate + "百度信息流广告的该日总Ecpm:" + sumEcpm_stream_Baidu);
                    log.info("日期:" + whatDate + "百度激励广告的该日总Ecpm:" + sumEcpm_encourage_Baidu);
                    log.info("日期:" + whatDate + "百度横幅广告的该日总Ecpm:" + sumEcpm_rowStyle_Baidu);

                    log.info("-----------------------------------------------------------------------------------");


                    adnName = "快手";
                    BigDecimal sumEcpm_rowStyle_KuaiShou = adnMapper.sumRowStyleEcpmCount(LocalDateTime.now(), LocalDateTime.now(), adnName, proxyName);
                    BigDecimal sumEcpm_encourage_KuaiShou = adnMapper.sumEncourageEcpmCount(LocalDateTime.now(), LocalDateTime.now(), adnName, proxyName);
                    BigDecimal sumEcpm_inscreen_KuaiShou = adnMapper.sumInscreenEcpmCount(startTime, endTime, adnName, proxyName);
                    BigDecimal sumEcpm_open_KuaiShou = adnMapper.sumOpenEcpmCount(startTime, endTime, adnName, proxyName);
                    BigDecimal sumEcpm_stream_KuaiShou = adnMapper.sumStreamEcpmCount(startTime, endTime, adnName, proxyName);
                    List<BigDecimal> bigDecimals1 = nullToZero(sumEcpm_rowStyle_KuaiShou, sumEcpm_encourage_KuaiShou, sumEcpm_inscreen_KuaiShou, sumEcpm_open_KuaiShou, sumEcpm_stream_KuaiShou);
                    sumEcpm_rowStyle_KuaiShou = bigDecimals1.get(0);
                    sumEcpm_encourage_KuaiShou = bigDecimals1.get(1);
                    sumEcpm_inscreen_KuaiShou = bigDecimals1.get(2);
                    sumEcpm_open_KuaiShou = bigDecimals1.get(3);
                    sumEcpm_stream_KuaiShou = bigDecimals1.get(4);

                    BigDecimal sumEcpm_total_KuaiShou = sumEcpm_rowStyle_KuaiShou.add(sumEcpm_encourage_KuaiShou).add(sumEcpm_inscreen_KuaiShou).add(sumEcpm_stream_KuaiShou).add(sumEcpm_open_KuaiShou);
                    TotalAdn adn_kuaiShou = TotalAdn.builder().totalAdnDate(whatDate).totalAdnType(adnName)
                            .totalAdnOpen(sumEcpm_open_KuaiShou).totalAdnRow(sumEcpm_rowStyle_KuaiShou)
                            .totalEncourage(sumEcpm_encourage_KuaiShou)
                            .totalAdnStream(sumEcpm_stream_KuaiShou)
                            .totalAdnIn(sumEcpm_inscreen_KuaiShou)
                            .totalAdnEcpm(sumEcpm_total_KuaiShou)
                            .appid(gameID)
                            .proxyName(proxyName)
                            .build();

                    log.info("日期:" + whatDate + "快手插屏广告的该日总Ecpm:" + sumEcpm_inscreen_KuaiShou);
                    log.info("日期:" + whatDate + "快手开屏广告的该日总Ecpm:" + sumEcpm_open_KuaiShou);
                    log.info("日期:" + whatDate + "快手信息流广告的该日总Ecpm:" + sumEcpm_stream_KuaiShou);
                    log.info("日期:" + whatDate + "快手激励广告的该日总Ecpm:" + sumEcpm_encourage_KuaiShou);
                    log.info("日期:" + whatDate + "快手横幅广告的该日总Ecpm:" + sumEcpm_rowStyle_KuaiShou);

                    //快手总ECPM
                    BigDecimal sumEcpm_KuaiShou_Total = sumEcpm_encourage_KuaiShou.add(sumEcpm_inscreen_KuaiShou).add(sumEcpm_open_KuaiShou).add(sumEcpm_stream_KuaiShou).add(sumEcpm_rowStyle_KuaiShou);

                    //优量汇 总ECPM
                    BigDecimal sumEcpm_YouLiangHui_Total = sumEcpm_encourage_YouLiangHui.add(sumEcpm_inscreen_YouLiangHui).add(sumEcpm_open_YouLiangHui).add(sumEcpm_stream_YouLiangHui).add(sumEcpm_rowStyle_YouLiangHui);

                    //穿山甲 总ECPM
                    BigDecimal sumEcpm_Gromore_Total = sumEcpm_encourage_Gromore.add(sumEcpm_inscreen_Gromore).add(sumEcpm_open_Gromore).add(sumEcpm_stream_Gromore).add(sumEcpm_rowStyle_Gromore);

                    //百度 总ECPM
                    BigDecimal sumEcpm_Baidu_Total = sumEcpm_encourage_Baidu.add(sumEcpm_inscreen_Baidu).add(sumEcpm_open_Baidu).add(sumEcpm_stream_Baidu).add(sumEcpm_rowStyle_Baidu);

                    BigDecimal sumAdvLegends_Total = sumEcpm_KuaiShou_Total.add(sumEcpm_YouLiangHui_Total).add(sumEcpm_Gromore_Total).add(sumEcpm_Baidu_Total);

                    //广告联盟 统计
                    TotalAdvlegends advlegends = TotalAdvlegends.builder().totalDate(whatDate).totalEcpm(sumAdvLegends_Total)
                            .baidu(sumEcpm_Baidu_Total).fastHand(sumEcpm_KuaiShou_Total)
                            .gromore(sumEcpm_Gromore_Total).youLiangHui(sumEcpm_YouLiangHui_Total)
                            .appid(gameID)
                            .proxyName(proxyName)
                            .build();


                    //ECPM统计

                    //插屏广告
                    BigDecimal sumTotal_Ecpm_inscreen = sumEcpm_inscreen_Gromore.add(sumEcpm_inscreen_YouLiangHui).add(sumEcpm_inscreen_Baidu).add(sumEcpm_inscreen_KuaiShou);
                    //激励广告
                    BigDecimal sumTotal_Ecpm_encourage = sumEcpm_encourage_Gromore.add(sumEcpm_encourage_YouLiangHui).add(sumEcpm_encourage_Baidu).add(sumEcpm_encourage_KuaiShou);
                    //信息流广告
                    BigDecimal sumTotal_Ecpm_stream = sumEcpm_stream_Gromore.add(sumEcpm_stream_YouLiangHui).add(sumEcpm_stream_Baidu).add(sumEcpm_stream_KuaiShou);
                    //开屏广告
                    BigDecimal sumTotal_Ecpm_open = sumEcpm_open_Gromore.add(sumEcpm_open_YouLiangHui).add(sumEcpm_open_Baidu).add(sumEcpm_open_KuaiShou);
                    //横幅广告
                    BigDecimal sumTotal_Ecpm_rowStyle = sumEcpm_rowStyle_Gromore.add(sumEcpm_rowStyle_YouLiangHui).add(sumEcpm_rowStyle_Baidu).add(sumEcpm_rowStyle_KuaiShou);
                    //总和
                    BigDecimal sumTotal_Ecpm_Total = sumTotal_Ecpm_inscreen.add(sumTotal_Ecpm_encourage).add(sumTotal_Ecpm_stream).add(sumTotal_Ecpm_open).add(sumTotal_Ecpm_rowStyle);

                    TotalEcpm totalEcpm = TotalEcpm.builder().totalEcpmDate(whatDate).totalEcpm(sumTotal_Ecpm_Total)
                            .totalEcpmIn(sumTotal_Ecpm_inscreen).totalEcpmEncourage(sumTotal_Ecpm_encourage)
                            .totalEcpmStream(sumTotal_Ecpm_stream).totalEcpmOpen(sumTotal_Ecpm_open)
                            .totalEcpmRow(sumTotal_Ecpm_rowStyle)
                            .appid(gameID)
                            .proxyName(proxyName)
                            .build();


                    SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);//创建一个具有批量处理功能的会话
                    try {
                        adnMapper.addTotalAdn(adn_baidu);               //百度    数据统计
                        adnMapper.addTotalAdn(adn_gromore);             //穿山甲   数据统计
                        adnMapper.addTotalAdn(adn_youlianghui);         //优量汇   数据统计
                        adnMapper.addTotalAdn(adn_kuaiShou);            //快手    数据统计

                        adnMapper.addTotalAdvLegends(advlegends); //广告联盟统计
                        adnMapper.addTotalEcpm(totalEcpm);        //ecpm统计

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
                        log.info(LocalDate.now() + proxyName + "_百度广告代理当日红包奖励统计:" + bd_Cash);
                        sumMapper.addProxyCommissionCount(sumProxy_ylh);
                        log.info(LocalDate.now() + proxyName + "_优量汇广告代理当日红包奖励统计:" + ylh_Cash);
                        sumMapper.addProxyCommissionCount(sumProxy_csj);
                        log.info(LocalDate.now() + proxyName + "_穿山甲广告代理当日红包奖励统计:" + csj_Cash);
                        sumMapper.addProxyCommissionCount(sumProxy_ks);
                        log.info(LocalDate.now() + proxyName + "_快手广告代理当日红包奖励统计:" + ks_Cash);


                        BigDecimal sumProxyCash = bd_Cash.add(ylh_Cash).add(csj_Cash).add(ks_Cash);
                        SumProxy sumProxy_all = SumProxy.builder()
                                .proxyName(proxyName)
                                .sumDay(LocalDate.now())
                                .adnName("all")
                                .sumProxyCash(sumProxyCash)
                                .build();
                        sumMapper.addProxyCommissionCount(sumProxy_all);
                        log.info(LocalDate.now() + proxyName + "_所有广告代理当日红包奖励统计:" + sumProxyCash);

                    } finally {
                        sqlSession.clearCache();
                        sqlSession.close();
                    }
                }
            }
        };

        scheduledExecutorService.execute(runnable);
        scheduledExecutorService.shutdown();
        log.info("记载在线用户任务已关闭");
    }
}
