package com.yunting.client.mapper;

import com.yunting.client.DTO.RetainActive;
import com.yunting.client.DTO.VO.RetainActiveVO;
import com.yunting.client.entity.DayBehaveRecordlist;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component("DayBehaveRecordlistMapper")
public interface DayBehaveRecordlistMapper {

    //    记录一条每日行为记录
    Integer addDayBehaveRecordlist(@Param("dayRecord") DayBehaveRecordlist dayBehaveRecordlist);

    //    通过playerId获取该玩家每天最新一条 行为记录   (因为是获取到的第一条,所以即使后面误加了行为记录，也因为查到的是第一条，而不会修改错数据)
    DayBehaveRecordlist getDayLastDayBehaveRecordlistByPlayerId(@Param("playerId") Long playerId);

    //    通过playerId获取该玩家所有行为记录
    List<DayBehaveRecordlist> getDayBehaveRecordlistByPlayerId(@Param("playerId") Long playerId);

    //    通过playerId获取该玩家所有行为记录
    List<DayBehaveRecordlist> getDayBehaveRecordlistByPlayerIdAndTime(@Param("playerId") Long playerId, @Param("recordTime") LocalDate recordTime);

    //    通过playerId获取该玩家一条每天的留存活跃记录
    List<RetainActiveVO> getRetainActiveByPlayerId(@Param("active") RetainActive retainActive);

    //    改变玩家当日行为记录 (当日激励观看总数)
    Integer changeDayBehaveRecordEncourageCount(@Param("dayid") Long dayId, @Param("encourageCount") Integer encourageCount);

    //    改变玩家当日行为记录 (当日服务端回调总数)
    Integer changeDayBehaveRecordCallbackCount(@Param("dayid") Long dayId, @Param("callbackCount") Integer callbackCount);

    //    改变玩家当日行为记录 (当日服务端回调且发放奖励总数 , 注册到此次红包总数 ,  当日红包总数)
    Integer changeDayBehaveRecordCallbackRewardCount(@Param("dayid") Long dayId, @Param("rewardCount") Integer rewardCount, @Param("totalRed") BigDecimal totalRed, @Param("todayRed") BigDecimal todayRed);

    //    改变玩家当日行为记录 (当日提现金额)
    Integer changeDayBehaveRecordWithdrawCash(@Param("dayid") Long dayId, @Param("dayCash") BigDecimal dayCash);


}