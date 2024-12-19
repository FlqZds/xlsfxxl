package com.yunting.adv.mapper.Adv;

import com.yunting.adv.entity.Adv.AdStream;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component("AdStreamMapper")
public interface AdStreamMapper {

    //    插入一条信息流广告 记录
    Integer insertAdStream(AdStream adStream);

    Long streamFirstClickTime(@Param("advId") String advId, @Param("firstClickTime") LocalDateTime firstClickTime);

    //      通过信息流id 修改点击量
    Integer changeAdStreamRecord(@Param("targetAdv") Long adEncourageId, @Param("changeData") AdStream adStream);

    List<AdStream> showAdStreamRecordlist(@Param("advType") Character advType);

    //  通过代码位ID拿到所有的激励记录
    List<AdStream> getAdStreamRecordsByCodeId(@Param("codeId") String codeId);

}