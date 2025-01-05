package com.yunting.sum.service;


import com.yunting.common.Dto.RiskControlSetting;
import com.yunting.common.exception.AppException;
import com.yunting.common.results.ResponseEnum;
import com.yunting.common.utils.RedisUtil_Record;
import com.yunting.common.utils.ST;
import com.yunting.common.utils.SpringRollBackUtil;
import com.yunting.sum.entity.SumCountNewplayer;
import com.yunting.sum.entity.SumProxy;
import com.yunting.sum.entity.setting.*;
import com.yunting.sum.entity.total_data.TotalAdn;
import com.yunting.sum.entity.total_data.TotalAdvlegends;
import com.yunting.sum.entity.total_data.TotalEcpm;
import com.yunting.sum.mapper.AdnMapper;
import com.yunting.sum.mapper.SettingUpdateMapper;
import com.yunting.sum.mapper.SumMapper;
import com.yunting.sum.util.ExecuteTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static com.yunting.common.utils.FS.*;

@Slf4j
@Component("DaySummaryCounter")
public class DaySummaryCounter {

    public static boolean isSave; //是否记录在线用户

    @Resource(name = "ExecuteTask")
    private ExecuteTask executeTask;


    //统计该日所有代理的游戏的广告数据

    /***
     * 在凌晨三点 每天统计前一日  所有代理 (包含子代)的 每种广告联盟的 广告奖励  ecpm
     */
    @Scheduled(cron = "0 0 3 * * ?")  //间隔一天   每天凌晨三点执行任务
    @Transactional(rollbackFor = Exception.class)
    public void getSummary() {
        log.info("开始统计前一日的代理数据和广告联盟数据");
        executeTask.task_summary_all();
    }


    /***
     * 每日十二点后延迟十秒
     * <P>
     * 1.从数据库读取新的游戏配置到redis
     * <P>
     * 2.统计该游戏所有一级代理 [自己+子代当日红包奖励统计]
     * <P>
     * 3.将次日留存记录清空<P>
     * 4.新建在线用户表
     *
     */
    //间隔一天
    @Scheduled(cron = "30 0 0 * * ?")
    //间隔一天  每天凌晨零点执行任务
    public void refreshGameSettingDaily() {
        log.info("开始读取新一日的游戏配置,同时统计游戏一级代理的数据");
        executeTask.task_refresh_GameSetting_Daily();
    }


    /***
     * 每日23:59分统计新增用户
     */

    @Resource(name = "SumMapper")
    private SumMapper sumMapper;

//    @Transactional(rollbackFor = Exception.class)
//    public void saveEverydayNewPlayer() {
//        BigDecimal allCash_day = BigDecimal.ZERO; //当日所有代理的红包奖励
//        List<String> OneProxyName = adnMapper.getAllProxyName();//一级
//        for (String proxyName : OneProxyName) {
//            allCash_day = allCash_day.add(adnMapper.getAllProxyCashCount(LocalDate.now(), proxyName));
//        }
//
//        //计算今天所有代理的分销佣金
//        for (String proxyName : OneProxyName) {
//            String proxyRatio = adnMapper.getProxyRatio(proxyName) + "";  //一级代理的比例
//
//            BigDecimal proxy_commission = allCash_day.multiply(new BigDecimal(proxyRatio)).multiply(Percentage);//分给该一级代理的总佣金
//
//            List<String> sonList = adnMapper.getAllProxySonNameByProxyName(proxyName);
//            for (String son : sonList) {
//                String sonRatio = adnMapper.getSonProxyRatio(son).toString(); //子代的比例
//                BigDecimal sonCash = adnMapper.getSumCashByProxyNameAndDate(son, LocalDate.now()).getSumProxyCash();//子代该日总奖励
//                BigDecimal son_commission = sonCash.multiply(new BigDecimal(sonRatio)).multiply(Percentage);//子代该日佣金
//                proxy_commission = proxy_commission.subtract(son_commission);
//            }
//
//            //该一级代理自己实际所得
//        }
//
//    }


}
