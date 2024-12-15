package com.yunting.client.mapper.Adv;

import com.yunting.client.DTO.SearchDto;
import com.yunting.client.DTO.VO.AdrecordVo;
import com.yunting.client.entity.Adv.AdRowstyle;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component("AdRowstyleMapper")

public interface AdRowstyleMapper {
    //    插入一条横幅广告 记录
    Integer insertAdRowstyle(AdRowstyle adRowstyle);

    //  加横幅首次点击时间
    Long rowStyleFirstClickTime(@Param("advId") String advId, @Param("firstClickTime") LocalDateTime firstClickTime);

    Integer changeAdRowRecord(@Param("targetAdv") Long targetAdv,@Param("changeData") AdRowstyle adRowstyle);

    List<AdRowstyle> showAdRowRecordlist(@Param("advType") Character advType);

    //  通过代码位ID拿到所有的横幅记录
    List<AdRowstyle> getAdRowstyleRecordsByCodeId(@Param("codeId") String codeId);

    List<AdrecordVo> queryAdRowstyleRecordBySearch(@Param("searchDto") SearchDto searchDto);
}