package com.yunting.sum.mapper;

import com.yunting.sum.entity.DayBehaveRecordlist;
import com.yunting.sum.entity.SumCountNewplayer;
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

@Component("SumMapper")
public interface SumMapper {

    //因为每日23:55分查询留存表,故此方案可行,否则需查player表  //根据留存表查到所有当天的哪个游戏的新用户
    Long getThisDayNewPlayer(@Param("proxyName") String proxyName, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("gameID") String gameID);

    //查询该代理的激励-某个广告联盟的 当日所有奖励 (只看有奖励的激励)
    BigDecimal getThisDayEncourageProxyCommission(@Param("proxyName") String proxyName, @Param("adnName") String adnName, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    //插入该日 新用户的统计数据
    Integer addNewPlayerCount(@Param("newPlayer") SumCountNewplayer countNewplayer);

    //插入该日的 某代理的(指定广告联盟)的分销佣金统计数据
    Integer addProxyCommissionCount(@Param("proxy") SumProxy sumProxy);

    //修改该日某代理的 分销佣金统计数据
    Integer modifyProxyCommissionCount(@Param("sumProxyAllCash") BigDecimal sumProxyAllCash, @Param("proxyName") String proxyName, @Param("sumDay") LocalDate sumDay);


    //    更新一条该用户的当日行为记录
    Integer updatePlayerDayrecord(@Param("playerId") Long playerId, @Param("retainTime") LocalDateTime retainTime, @Param("pos") String pos, @Param("date") LocalDate date);

    //    更新玩家位置信息
    int updateLocation(@Param("position") String position, @Param("recordTime") LocalDateTime recordTime, @Param("playerId") String playerId);


}
