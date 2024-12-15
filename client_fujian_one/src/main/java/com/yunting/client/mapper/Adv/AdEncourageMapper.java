package com.yunting.client.mapper.Adv;

import com.yunting.client.DTO.AdEncourageDto;
import com.yunting.client.DTO.AdEncourageLoadDto;
import com.yunting.client.DTO.CallbackDto;
import com.yunting.client.DTO.SearchDto;
import com.yunting.client.DTO.VO.AdEncourageVo;
import com.yunting.client.DTO.VO.TableVo;
import com.yunting.client.entity.Adv.AdEncourage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component("AdEncourageMapper")
public interface AdEncourageMapper {

    // 通过主键拿到指定广告记录
    AdEncourage selectByPrimaryKey(@Param("advEncourageId") Long advEncourageId);

    //    添加激励广告同时 获取 新建的激励广告id
    Integer insertAdEncourage(AdEncourage adEncourage);

    //    加载广告上传记录 (来源,请求时间,开红包按压时长)
    Integer loadAndChange(@Param("load") AdEncourageLoadDto loadDto);

    //    观看广告上传记录  (ecpm,错误信息,请求id,代码位id,是否展示)
    Integer watchAndChange(@Param("watch") AdEncourageDto encourageDto);


    //      通过激励广告id 修改该条激励广告记录  (服务端回调 交易id,服务端回调时间)
    Integer changeAdEncourageRecordServcer(@Param("adEncourageId") Long adEncourageId,@Param("transId") String transId,@Param("serverTime")LocalDateTime serverTime);


    //      通过激励广告id 修改该条激励广告记录  (客户端回调时间,transID) (达到奖励条件)
    Integer changeAdEncourageRecordEnoughReward(@Param("adEncourageId") String adEncourageId,@Param("clientTime")String clientTime,@Param("transId")String transId);

    //      通过激励广告id 修改该条激励广告记录  (是否完成观看) is_see_end
    Integer changeAdEncourageRecordIsOk(@Param("targetAdv") Long adEncourageId,@Param("changeData") String isSeeEnd);

//          激励广告 首次点击时间
    Integer changeAdEncourageRecordClickTime(@Param("targetAdv") String adEncourageId,@Param("clickTime") LocalDateTime clickTime,@Param("clickCount") Integer clickCount);

    //      通过激励广告id 修改该条激励广告记录  (点击次数+是否关闭广告+ 违规操作信息) (关闭广告)
    Integer changeAdEncourageRecordClose(@Param("targetAdv") Long adEncourageId,@Param("clickCount") Integer clickCount,@Param("exceptionMsg")String exceptionMsg);

    //      通过激励id获取是否关闭广告
    String isCloseAdEncourage(@Param("targetAdv") Long adEncourageId);

    //      通过激励广告id 修改该条激励广告点击量 (补发点击量)
    Integer compensateAdEncourageClickCount(@Param("targetAdv") Long adEncourageId,@Param("clickCount") Integer clickCount);

    //      通过激励广告id 修改该条激励广告记录  (是否颁发奖励) 广告奖励
    Integer changeAdEncourageRecordReward(@Param("targetAdv") Long adEncourageId,@Param("encourageReward") BigDecimal encourageReward);

    //      通过激励广告id 修改该条激励广告记录  (收下奖励) 开红包按压时长
    Integer changeAdEncourageRecordGetWard(@Param("targetAdv") String adEncourageId,@Param("askPutTimeDate") String askPutTimeDate);

    //  展示所有激励广告的记录
    List<AdEncourage> showAdEncourageRecordlist(@Param("advType") String advType);

    //  通过代码位ID拿到所有的激励记录
    List<AdEncourage> getAdEncourageRecordsByCodeId(@Param("codeId") String codeId);

    //  根据用户的搜索条件筛选出激励广告的记录
    List<AdEncourageVo> queryAdEncourageRecordBySearch(@Param("searchDto") SearchDto searchDto);

    //    通过交易id找到激励广告的记录
    CallbackDto findAdEncourageByTransId(@Param("transId") Long transId);

    // 获取该表数据大小
    TableVo getTableSize(@Param("tableName") String tableName);
}