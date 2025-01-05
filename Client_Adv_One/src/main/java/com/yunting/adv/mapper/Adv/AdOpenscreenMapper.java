package com.yunting.adv.mapper.Adv;

import com.yunting.adv.entity.Adv.AdOpenscreen;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component("AdOpenscreenMapper")
public interface AdOpenscreenMapper {
    //    插入一条开屏广告 记录
    Long insertOpenscreen(AdOpenscreen adOpenscreen);

    //  加横幅首次点击时间,首次点击次数
    Long openScreenFirstClickTime(@Param("advId") String advId, @Param("firstClickTime") LocalDateTime firstClickTime);

    Integer changeAdOpenRecord(@Param("targetAdv") Long targetAdv, @Param("changeData") AdOpenscreen adOpenscreen);


}