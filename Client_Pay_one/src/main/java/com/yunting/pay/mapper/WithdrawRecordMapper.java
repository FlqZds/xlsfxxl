package com.yunting.pay.mapper;

import com.yunting.pay.entity.WithdrawRecord;
import com.yunting.pay.entity.WithdrawSetting;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component("WithdrawRecordMapper")
public interface WithdrawRecordMapper {

    //获取该游戏最低返现金额
    String getLimitRebackMoney();

    //获取该游戏的对应金额的返现比例
    String getThisReback(@Param("money") BigDecimal money);

    // 获取提现配置
    WithdrawSetting getWithdrawSetting(Long gameId);

    //    插入一条提现记录
    Integer insertWithdrawRecord(WithdrawRecord record);

}