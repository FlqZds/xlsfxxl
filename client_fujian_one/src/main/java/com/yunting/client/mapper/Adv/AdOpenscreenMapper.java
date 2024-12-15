package com.yunting.client.mapper.Adv;

import com.yunting.client.DTO.SearchDto;
import com.yunting.client.DTO.VO.AdrecordVo;
import com.yunting.client.entity.Adv.AdOpenscreen;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component("AdOpenscreenMapper")
public interface AdOpenscreenMapper {
    //    插入一条开屏广告 记录
    Long insertOpenscreen(AdOpenscreen adOpenscreen);

    //  加横幅首次点击时间
    Long openScreenFirstClickTime(@Param("advId") String advId, @Param("firstClickTime") LocalDateTime firstClickTime);

    Integer changeAdOpenRecord(@Param("targetAdv") Long targetAdv,@Param("changeData") AdOpenscreen adOpenscreen);

    List<AdOpenscreen> showAdOpenscreenRecordlist(@Param("advType") Character advType);

    //  通过代码位ID拿到所有的激励记录
    List<AdOpenscreen> getAdOpenscreenRecordsByCodeId(@Param("codeId") String codeId);

    List<AdrecordVo> queryAdOpenscreenRecordBySearch(@Param("searchDto") SearchDto searchDto);
}