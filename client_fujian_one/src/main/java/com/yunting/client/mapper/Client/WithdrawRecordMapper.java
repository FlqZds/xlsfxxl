package com.yunting.client.mapper.Client;

import com.yunting.client.DTO.VO.WithdrawVo;
import com.yunting.client.entity.WithdrawRecord;
import com.yunting.client.entity.setting.WithdrawSetting;
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

    //  更新提现订单的状态
    Integer updatePayStatus(@Param("id") Long id, @Param("status") String status);

    // 获取该用户的当日提现次数
    Integer getTodayWithdrawCount(@Param("packageName") String pakcageName, @Param("playerId") Long playerId, @Param("begin") LocalDateTime todayBegin, @Param("end") LocalDateTime todayEnd);

    //获取该游戏的提现比例
    String getWithdrawPercentage(@Param("gameID") Long gameID);

    //根据玩家id,包名,获取该玩家的所有提现记录
    List<WithdrawVo> getWithdrawRecordByPlayerIdAndPackageName(@Param("playerId") Long playerId, @Param("packageName") String packageName);

}