package com.yunting.adv.mapper;

import com.yunting.adv.entity.DayBehaveRecordlist;
import com.yunting.adv.entity.UserRewardSetting;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component("DayBehaveRecordlistMapper")
public interface DayBehaveRecordlistMapper {

    //    通过playerId获取该玩家每天最新一条 行为记录   (因为是获取到的第一条,所以即使后面误加了行为记录，也因为查到的是第一条，而不会修改错数据)
    DayBehaveRecordlist getDayLastDayBehaveRecordlistByPlayerId(@Param("playerId") Long playerId);

    //    改变玩家当日行为记录 (当日激励观看总数)
    Integer changeDayBehaveRecordEncourageCount(@Param("dayid") Long dayId, @Param("encourageCount") Integer encourageCount);

    //    改变玩家当日行为记录 (当日服务端回调且发放奖励总数 , 注册到此次红包总数 , 当日红包总数)
    Integer changeDayBehaveRecordCallbackRewardCount(@Param("dayid") Long dayId, @Param("rewardCount") Integer rewardCount, @Param("totalRed") BigDecimal totalRed, @Param("todayRed") BigDecimal todayRed);

    //    改变玩家当日行为记录 (当日无服务端回调数量)
    Integer changeDayBehaveRecordNoCallbackCount(@Param("dayid") Long dayId, @Param("noCallbackCount") Integer noCallbackCount);

    //    改变玩家当日行为记录 (服务端回调未发放奖励总数)
    Integer changeDayBehaveRecordNoRewardCount(@Param("dayid") Long dayId, @Param("noRewardCount") Integer noRewardCount);

    //    改变玩家当日行为记录 (服务端回调默认未发放奖励总数)
    Integer changeDayBehaveRecordDefaultNoneRewardCount(@Param("dayid") Long dayId, @Param("noneReward") Integer noneReward);


    //获取奖励设置
    UserRewardSetting getUserRewardSettingByGameID(@Param("gameId") Long gameId);

}