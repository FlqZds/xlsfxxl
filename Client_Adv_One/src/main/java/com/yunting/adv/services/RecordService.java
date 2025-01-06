package com.yunting.adv.services;

import com.yunting.adv.dto.AdDto;
import com.yunting.adv.dto.AdEncourageDto;
import com.yunting.adv.dto.AdEncourageLoadDto;
import com.yunting.adv.dto.CallbackMessage;
import com.yunting.adv.entity.Adv.AdInscreen;
import com.yunting.adv.entity.Adv.AdOpenscreen;
import com.yunting.adv.entity.Adv.AdRowstyle;
import com.yunting.adv.entity.Adv.AdStream;
import com.yunting.common.Dto.PlayerDTO;
import com.yunting.common.results.ResultMessage;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;


public interface RecordService {

    /***
     * 存储横幅广告的记录
     * @param rowstyle  横幅广告表
     *
     */
    public Long SaveRowStyleRecord(HttpServletRequest request, PlayerDTO playerDTO, AdDto rowstyle);

    /***
     * 存储插屏广告的记录
     * @param adInscreen 插屏广告表
     *
     */
    public Long SaveAdInscreenRecord(HttpServletRequest request, PlayerDTO playerDTO, AdDto adInscreen);

    /***
     * 存储开屏广告的记录
     * @param adOpenscreen 开屏广告表
     *
     */
    public Long SaveAdOpenscreenRecord(HttpServletRequest request, PlayerDTO playerDTO, AdDto adOpenscreen);

    /***
     * 存储信息流广告的记录
     * @param adStream 信息流广告表
     *
     */
    public Long SaveAdStreamRecord(HttpServletRequest request, PlayerDTO playerDTO, AdDto adStream);

    //加载广告之前是激励验证

    /***
     * 加载到激励广告得到requestID上传广告记录
     *
     * @param loadDto 加载的激励传输对象
     * @return
     */
    public void loadAndUpload(AdEncourageLoadDto loadDto, PlayerDTO playerDTO);

    /***
     * 观看广告上传激励广告记录
     *
     */
    public Long watchAndUpload(AdEncourageDto adEncourageDto);


    /***
     * 服务端回调
     * @param sign 签名
     * @param user_id 广告用户id
     * @param trans_id 交易id
     * @param reward_name 奖励名称
     * @param reward_amount 奖励数量
     * @param extra 额外信息
     *
     */
    public CallbackMessage serverCallBackIsReward(String sign, String user_id, String trans_id, String reward_name, int reward_amount, String extra);


    /***
     * 达到奖励条件修改激励广告的记录
     * @param advEncourageId 要修改的激励广告id
     * @param trasnId 交易id
     */
    public void enoughchangeAdEncourageRecord(PlayerDTO playerDTO, String advEncourageId, String trasnId);

    /***
     * 完成观看激励广告  修改激励的记录
     * @param advEncourageId 要修改的激励广告id
     *
     */
    public void isOKchangeAdEncourageRecord(Long advEncourageId);

    /***
     * 点击广告 记录点击量,首次点击时间，
     * @param advId 广告的id
     *
     */
    public Long clickCountAndFirstClickTime(String advId);


    /***
     * 停止记录广告的点击量 (关闭广告接口)
     * @param advEncourageId 要补齐的广告记录的广告ID
     * @param  clickCount 点击次数
     * @param exceptionMsg 违规操作
     * @param isRemedy 是否为补发 (0是补发,1是正常关闭)
     * return 激励广告的红包值  其他几个广告就是-1
     */
    public ResultMessage closeRecordingClick(PlayerDTO playerDTO, String advEncourageId, Integer clickCount, String exceptionMsg, String isRemedy);


    /***
     * 激励广告收下奖励接口
     * @param advEncourageId
     */
    public void getWardEncourage(String advEncourageId, String getWardTimeDate);

}
