package com.yunting.sum.mapper;

import com.yunting.sum.entity.SumProxy;
import com.yunting.sum.entity.total_data.TotalAdn;
import com.yunting.sum.entity.total_data.TotalAdvlegends;
import com.yunting.sum.entity.total_data.TotalEcpm;
import com.yunting.sum.entity.total_data.TotalProfile;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component("AdnMapper")
public interface AdnMapper {
    //查到该代理的比例
    Double getProxyRatio(@Param("proxyName") String proxyName);

    //查到该子代理的比例
    Double getSonProxyRatio(@Param("proxyName") String proxyName);


    //查到该游戏的所有一级代理
    List<String> getAllProxyName();

    //查到该游戏的所有二级代理
    List<String> getAllProxySonName();

    //根据代理名称查到该一级代理所有二级代理
    List<String> getAllProxySonNameByProxyName(@Param("proxyName") String proxyName);

    //统计当日所有代理的红包奖励
    BigDecimal getAllProxyCashCount(@Param("date") LocalDate date, @Param("proxyName") String proxyName);

    //根据名称找到该日 该代理的佣金统计数据
    SumProxy getSumCashByProxyNameAndDate(@Param("proxyName") String proxyName, @Param("date") LocalDate date);

    //    根据日期和广告网络 统计插屏广告ecpm (广告联盟)
    BigDecimal sumInscreenEcpmCount(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("adnName") String adnName, @Param("proxyName") String proxyName);

    //    根据日期和广告网络 统计激励广告ecpm (广告联盟)
    BigDecimal sumEncourageEcpmCount(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("adnName") String adnName, @Param("proxyName") String proxyName);


    //    根据日期和广告网络 统计开屏广告ecpm (广告联盟)
    BigDecimal sumOpenEcpmCount(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("adnName") String adnName, @Param("proxyName") String proxyName);


    //    根据日期和广告网络 统计横幅广告ecpm (广告联盟)
    BigDecimal sumRowStyleEcpmCount(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("adnName") String adnName, @Param("proxyName") String proxyName);


    //    根据日期和广告网络 统计信息流广告ecpm (广告联盟)
    BigDecimal sumStreamEcpmCount(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("adnName") String adnName, @Param("proxyName") String proxyName);


    //统计并插入该代理的 该种广告类型 的广告网络的ecpm (广告联盟)
    Integer addTotalAdn(@Param("adnTotal") TotalAdn adnTotal);

    Integer addTotalAdvLegends(@Param("advlegends") TotalAdvlegends advlegends);

    Integer addTotalProfile(@Param("profile") TotalProfile profile);

    Integer addTotalEcpm(@Param("totalEcpm") TotalEcpm totalEcpm);


}
