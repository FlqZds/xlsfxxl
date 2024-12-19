package com.yunting.adv.mapper.Adv;

import com.yunting.adv.entity.Adv.AdInscreen;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component("AdInscreenMapper")
public interface AdInscreenMapper {
    //    插入一条插屏流广告 记录
    Integer insertAdInscreen(AdInscreen adInscreen);

    //  加插屏首次点击时间
    Long inScreenFirstClickTime(@Param("advId") String advId, @Param("firstClickTime") LocalDateTime firstClickTime);

    Integer changeAdInScreenRecord(@Param("targetAdv") Long targetAdv,@Param("changeData") AdInscreen adInscreen);

//    查到所有的插屏广告记录
    List<AdInscreen> showAdInscreenRecordlist(@Param("advType") Character advType);

    //  通过代码位ID拿到所有的激励记录
    List<AdInscreen> getAdInscreenRecordsByCodeId(@Param("codeId") String codeId);


}