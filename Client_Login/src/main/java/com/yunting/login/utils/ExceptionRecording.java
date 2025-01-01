package com.yunting.login.utils;

import com.yunting.common.Dto.ExceptionRecordlsit;
import com.yunting.login.mapper.LocationMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Slf4j
@Component("ExceptionRecording")
public class ExceptionRecording {

    @Resource(name = "LocationMapper")
    LocationMapper locationMapper;

    /***
     * 上传异常信息
     * @param exceptType 异常类型
     * @param args 可变参数 (玩家id,游戏id,昵称,设备品牌,类型,anID,oaID,错误信息)
     */
    @Transactional(rollbackFor = Exception.class)
    public void  uploadException(String exceptType,String... args)
    {
        ExceptionRecordlsit exceptionRecord = ExceptionRecordlsit.builder()
                .playerId(args[0]).gameId(args[1]).wxNickName(args[2])

                .deviceType(args[3])
                .deviceDetail(args[4])
                .androidId(args[5])
                .oaid(args[6])

                .exceptType(exceptType)
                .exceptInfo(args[7]).exceptionTime(LocalDateTime.now())
                .build();
        locationMapper.insertExceptionRecord(exceptionRecord);
        log.error("当前用户id为:" + args[0] + "的异常信息已记录");
    }

}
