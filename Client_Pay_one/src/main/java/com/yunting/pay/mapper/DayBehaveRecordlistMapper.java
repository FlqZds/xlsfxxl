package com.yunting.pay.mapper;

import com.yunting.pay.entity.DayBehaveRecordlist;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component("DayBehaveRecordlistMapper")
public interface DayBehaveRecordlistMapper {

    //    通过playerId获取该玩家每天最新一条 行为记录   (因为是获取到的第一条,所以即使后面误加了行为记录，也因为查到的是第一条，而不会修改错数据)
    DayBehaveRecordlist getDayLastDayBehaveRecordlistByPlayerId(@Param("playerId") Long playerId);

    //    改变玩家当日行为记录 (当日提现金额)
    Integer changeDayBehaveRecordWithdrawCash(@Param("dayid") Long dayId, @Param("dayCash") BigDecimal dayCash);


}