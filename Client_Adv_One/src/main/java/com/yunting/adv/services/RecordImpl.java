package com.yunting.adv.services;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson2.JSONObject;
import com.yunting.adv.dto.*;
import com.yunting.adv.entity.Adv.*;
import com.yunting.adv.entity.Player;
import com.yunting.adv.mapper.Adv.*;
import com.yunting.adv.mapper.DayBehaveRecordlistMapper;
import com.yunting.adv.mapper.PlayerMapper;
import com.yunting.common.Dto.PlayerDTO;
import com.yunting.common.exception.AppException;
import com.yunting.common.results.ResponseEnum;
import com.yunting.common.results.ResultMessage;
import com.yunting.common.utils.IpUtils;
import com.yunting.common.utils.RedisUtil_Record;
import com.yunting.common.utils.ST;
import com.yunting.common.utils.SpringRollBackUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.yunting.common.utils.FS.Percentage;

@Slf4j
@Service("RecordImpl")
public class RecordImpl implements RecordService {

    @Resource(name = "PlayerMapper")
    private PlayerMapper playerMapper;

    @Resource(name = "RedisUtil_Record")
    private RedisUtil_Record rur;

    @Resource(name = "AdRowstyleMapper")
    private AdRowstyleMapper adRowstyleMapper;

    @Resource(name = "ST")
    private ST st;


    @Transactional
    public Long SaveRowStyleRecord(HttpServletRequest request, PlayerDTO playerDTO, AdDto adDto) {

        String ipAddr = IpUtils.getIpAddr(request);  //ip

        Long playerId = playerDTO.getPlayerId();

        Player player = playerMapper.selectPlayerByPlayerId(playerId);

        AdRowstyle rowstyle = AdRowstyle.builder().advTypeId('4')
                .playerId(playerId)
                .appId(playerDTO.getGameId().toString())
                .codeBitId(adDto.getCodeBitId())
                .requestId(adDto.getRequestId())
                .requestTime(LocalDateTime.now())
                .advEcpm(adDto.getAdvEcpm())
                .ip(ipAddr)
                .clicks(0)
                .wxNickName(player.getWxNickname())
                .errinfo("")
                .build();

        try {
            Integer integer = adRowstyleMapper.insertAdRowstyle(rowstyle);
            if (integer != 1) {
                log.error("添加横幅广告失败，请联系管理员");
                throw new AppException(ResponseEnum.SAVE_AD_ROWSTYLE_FAILED);
            }
            log.info("添加横幅广告，广告ID：{}", rowstyle.getAdRowStyleId());
            rur.set(rowstyle.getAdRowStyleId() + "advType", rowstyle.getAdvTypeId() + "");  //存 这个广告的类型
            return rowstyle.getAdRowStyleId();
        } catch (AppException e) {
            e.printStackTrace();
            SpringRollBackUtil.rollBack();
            log.error("不可抗因素导致的横幅广告记录失败");
            throw new AppException(ResponseEnum.SAVE_AD_ROWSTYLE_FAILED);
        }
    }

    @Resource(name = "AdInscreenMapper")
    private AdInscreenMapper adInscreenMapper;

    @Transactional
    public Long SaveAdInscreenRecord(HttpServletRequest request, PlayerDTO playerDTO, AdDto adDto) {

        String ipAddr = IpUtils.getIpAddr(request);  //ip

        Long playerId = playerDTO.getPlayerId();

        Player player = playerMapper.selectPlayerByPlayerId(playerId);

        AdInscreen adInscreen = AdInscreen.builder().advTypeId('2')
                .playerId(playerId)
                .appId(playerDTO.getGameId().toString())
                .codeBitId(adDto.getCodeBitId())
                .requestId(adDto.getRequestId())
                .requestTime(LocalDateTime.now())
                .advEcpm(adDto.getAdvEcpm())
                .ip(ipAddr)
                .clicks(0)
                .wxNickName(player.getWxNickname())
                .errinfo("")
                .build();

        try {
            Integer integer = adInscreenMapper.insertAdInscreen(adInscreen);
            if (integer != 1) {
                log.error("添加插屏广告失败，请联系管理员");
                throw new AppException(ResponseEnum.SAVE_AD_INSCREEN_FAILED);
            }
            log.info("添加插屏广告，广告ID：{}", adInscreen.getAdInscreenId());
            rur.set(adInscreen.getAdInscreenId() + "advType", adInscreen.getAdvTypeId() + "");  //存 这个广告的类型
            return adInscreen.getAdInscreenId();
        } catch (AppException e) {
            e.printStackTrace();
            SpringRollBackUtil.rollBack();
            log.error("不可抗因素导致的插屏广告记录更新失败");
            throw new AppException(ResponseEnum.SAVE_AD_INSCREEN_FAILED);
        }
    }

    @Resource(name = "AdOpenscreenMapper")
    private AdOpenscreenMapper adOpenscreenMapper;

    @Transactional
    public Long SaveAdOpenscreenRecord(HttpServletRequest request, PlayerDTO playerDTO, AdDto adDto) {

        String ipAddr = IpUtils.getIpAddr(request);  //ip

        Long playerId = playerDTO.getPlayerId();

        Player player = playerMapper.selectPlayerByPlayerId(playerId);

        AdOpenscreen adOpenscreen = AdOpenscreen.builder().advTypeId('1')
                .playerId(playerId)
                .appId(playerDTO.getGameId().toString())
                .codeBitId(adDto.getCodeBitId())
                .requestId(adDto.getRequestId())
                .requestTime(LocalDateTime.now())
                .advEcpm(adDto.getAdvEcpm())
                .ip(ipAddr)
                .clicks(0)
                .wxNickName(player.getWxNickname())
                .errinfo("")
                .build();

        try {
            Long integer = adOpenscreenMapper.insertOpenscreen(adOpenscreen);
            if (integer != 1) {
                log.error("添加开屏广告失败，请联系管理员");
                throw new AppException(ResponseEnum.SAVE_AD_OPENSCREEN_FAILED);
            }
            log.info("添加开屏广告，广告ID：{}", adOpenscreen.getAdOpenscreenId());
            rur.set(adOpenscreen.getAdOpenscreenId() + "advType", adOpenscreen.getAdvTypeId() + "");  //存 这个广告的类型

            return adOpenscreen.getAdOpenscreenId();
        } catch (AppException e) {
            e.printStackTrace();
            SpringRollBackUtil.rollBack();
            log.error("不可抗因素导致的开屏广告记录更新失败");
            throw new AppException(ResponseEnum.SAVE_AD_OPENSCREEN_FAILED);
        }
    }

    @Resource(name = "AdStreamMapper")
    private AdStreamMapper adStreamMapper;

    @Transactional
    public Long SaveAdStreamRecord(HttpServletRequest request, PlayerDTO playerDTO, AdDto adDto) {

        String ipAddr = IpUtils.getIpAddr(request);  //ip

        Long playerId = playerDTO.getPlayerId();

        Player player = playerMapper.selectPlayerByPlayerId(playerId);

        AdStream adStream = AdStream.builder().advTypeId('3')
                .playerId(playerId)
                .appId(playerDTO.getGameId().toString())
                .codeBitId(adDto.getCodeBitId())
                .requestId(adDto.getRequestId())
                .requestTime(LocalDateTime.now())
                .advEcpm(adDto.getAdvEcpm())
                .ip(ipAddr)
                .clicks(0)
                .wxNickName(player.getWxNickname())
                .errinfo("")
                .build();

        try {
            Integer integer = adStreamMapper.insertAdStream(adStream);
            if (integer != 1) {
                log.error("添加信息流广告失败，请联系管理员");
                throw new AppException(ResponseEnum.SAVE_AD_STREAM_FAILED);
            }
            log.info("添加信息流广告，广告ID：{}", adStream.getAdStreamId());
            rur.set(adStream.getAdStreamId() + "advType", adStream.getAdvTypeId() + "");
            return adStream.getAdStreamId();
        } catch (AppException e) {
            e.printStackTrace();
            SpringRollBackUtil.rollBack();
            log.error("不可抗因素导致的信息流广告记录更新失败");
            throw new AppException(ResponseEnum.SAVE_AD_STREAM_FAILED);
        }
    }

    @Resource(name = "AdEncourageMapper")
    private AdEncourageMapper adEncourageMapper;

    @Transactional(rollbackFor = Exception.class)
    public Long watchAndUpload(AdEncourageDto adEncourageDto) {
        Long encourageId = adEncourageDto.getAdvEncourageId();
        rur.set(encourageId + "advType", "5");  //存 这个广告的类型
        try {
            adEncourageMapper.watchAndChange(adEncourageDto);  //是否展示 + 代码位id + 请求id + ecpm + 错误信息
            log.info("激励广告观看步骤修改成功,激励广告ID:" + encourageId);
            return encourageId;
        } catch (AppException e) {
            log.error("激励广告观看步骤修改失败，请联系管理员");
            SpringRollBackUtil.rollBack();
            throw new AppException(ResponseEnum.UPDATE_AD_ENCOURAGE_WATCH_FAILED);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void loadAndUpload(AdEncourageLoadDto loadDto, PlayerDTO playerDTO) {
        try {
            loadDto.setRequestTime(LocalDateTime.now().toString());

            Long playerId = playerDTO.getPlayerId();
            String from = loadDto.getEncourageFrom();
            if (from.equals("提现激励")) {
                rur.set(playerId + "withdraw", "0");
            }

            if (from.equals("正常激励") && rur.get(playerId + "withdraw") != null) {  //上一个 红包提现的视频看了却提现失败或者没去提现,然后来看普通激励了
                rur.delete(playerId + "withdraw");
            }

            adEncourageMapper.loadAndChange(loadDto);
            Long encourageId = loadDto.getAdvEncourageId();
            //          有了客户端回调,可以计数一次该玩家激励广告观看次数了
            Long dayId = dayBehaveRecordMapper.getDayLastDayBehaveRecordlistByPlayerId(playerDTO.getPlayerId()).getDayId();
            dayBehaveRecordMapper.changeDayBehaveRecordEncourageCount(dayId, 1);
            log.info("激励广告加载广告步骤修改成功,激励广告ID:" + encourageId);
        } catch (AppException e) {
            e.printStackTrace();
            log.error("激励广告加载广告步骤修改失败，请联系管理员");
            SpringRollBackUtil.rollBack();
            throw new AppException(ResponseEnum.UPDATE_AD_ENCOURAGE_LOAD_FAILED);
        }
    }


    @Value("${appSecurityKey}")
    private String appSecurityKey;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CallbackMessage serverCallBackIsReward(String sign, String user_id, String trans_id, String reward_name, int reward_amount, String extra) {

        ServerVo serverVo = JSONObject.parseObject(extra, ServerVo.class);
        Long adencourageId = serverVo.getRewardId();

//        找到该条激励广告的记录
        AdEncourage adEncourage = adEncourageMapper.selectByPrimaryKey(adencourageId);

        if (Objects.isNull(adEncourage)) {
            log.error("该条激励广告记录不存在,回调失败");
            return new CallbackMessage(false, 50001);
        }

//        奖励内容添加 ，  交易id添加

        //  sha256 签名appSecurityKey
        String sign_data = appSecurityKey + ":" + trans_id;

        String sign_raw = DigestUtil.sha256Hex(sign_data);

        if (sign.equals(sign_raw) == false) {
            log.error("激励广告回传签名不匹配，请联系管理员");
            return new CallbackMessage(false, 50001);
        }

        try {
            adEncourageMapper.changeAdEncourageRecordServcer(adencourageId, trans_id, LocalDateTime.now());
        } catch (Exception e) {
            SpringRollBackUtil.rollBack();
            log.error("修改激励广告服务端回调状态失败，请联系管理员");
            return new CallbackMessage(false, 50001);
        }

//        返回结果
        log.info("激励广告回传校验通过,服务端回调完成");
        return new CallbackMessage(true, -1);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enoughchangeAdEncourageRecord(PlayerDTO playerDTO, String advEncourageId, String trasnId) {

        Long playerId = playerDTO.getPlayerId();
        try {
            String clientTime = LocalDateTime.now().toString();
            adEncourageMapper.changeAdEncourageRecordEnoughReward(advEncourageId, clientTime, trasnId);

            log.info("玩家:|-" + playerId + "-|达到奖励条件修改激励广告的记录,达到奖励条件 步骤成功");
        } catch (Exception e) {
            SpringRollBackUtil.rollBack();
            log.error("玩家:|-" + playerId + "-|激励广告达到奖励条件修改失败，请联系管理员");
            throw new AppException(ResponseEnum.UPDATE_AD_ENCOURAGE_ENOUGH_FAILED);
        }
    }

    @Resource(name = "DayBehaveRecordlistMapper")
    private DayBehaveRecordlistMapper dayBehaveRecordMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultMessage closeRecordingClick(PlayerDTO playerDTO, String advId, Integer clickCount, String exceptionMsg, String isRemedy) {

        String advType = String.valueOf(advId.charAt(0));

        Long advID = Long.parseLong(advId);

        BigDecimal redValue = new BigDecimal(-1);//返回的红包值 -1代表不是激励的红包值
        switch (advType) {
            case "1": //开屏广告
            {
                try {
                    AdOpenscreen adOpenscreen = AdOpenscreen.builder().clicks(clickCount).build();
                    adOpenscreenMapper.changeAdOpenRecord(advID, adOpenscreen);
                } catch (Exception e) {
                    SpringRollBackUtil.rollBack();
                    log.error("修改开屏广告关闭状态失败，请联系管理员");
                    throw new AppException(ResponseEnum.UPDATE_AD_OPEN_CLOSE_FAILED);
                }
                log.info(advID + "<修改开屏广告关闭>操作成功");
                break;
            }
            case "2": //插屏广告
            {
                try {
                    AdInscreen adInscreen = AdInscreen.builder().clicks(clickCount).build();
                    adInscreenMapper.changeAdInScreenRecord(advID, adInscreen);
                } catch (Exception e) {
                    SpringRollBackUtil.rollBack();
                    log.error("修改插屏广告关闭状态失败，请联系管理员");
                    throw new AppException(ResponseEnum.UPDATE_AD_IN_CLOSE_FAILED);
                }
                log.info(advID + "<修改插屏广告关闭>操作成功");
                break;
            }
            case "3": //信息流广告
            {
                try {
                    AdStream adStream = AdStream.builder().clicks(clickCount).build();
                    adStreamMapper.changeAdStreamRecord(advID, adStream);
                } catch (Exception e) {
                    SpringRollBackUtil.rollBack();
                    log.error("修改信息流广告关闭状态失败，请联系管理员");
                    throw new AppException(ResponseEnum.UPDATE_AD_STREAM_CLOSE_FAILED);
                }
                log.info(advID + "<修改信息流广告关闭>操作录成功");
                break;
            }
            case "4": //横幅广告
            {
                try {
                    AdRowstyle adRowstyle = AdRowstyle.builder().clicks(clickCount).build();
                    adRowstyleMapper.changeAdRowRecord(advID, adRowstyle);
                } catch (Exception e) {
                    SpringRollBackUtil.rollBack();
                    log.error("修改横幅广告关闭状态失败，请联系管理员");
                    throw new AppException(ResponseEnum.UPDATE_AD_ROWSTYLE_CLOSE_FAILED);
                }
                log.info(advID + "<修改横幅广告关闭>操作成功");
                break;
            }
            case "5": //激励广告
            {
                Integer rewardCount = 0;
                Integer withdrawCount = 0;
                AdEncourage adEncourage = adEncourageMapper.selectByPrimaryKey(advID);
                if (Objects.equals(isRemedy, "0")) {
                    log.info("激励广告未正常关闭,补发点击量");
                    adEncourageMapper.compensateAdEncourageClickCount(advID, clickCount, exceptionMsg);
                    break;
                }

                if (Objects.equals(isRemedy, "1")) { //正常关闭广告

//判断是否给用户奖励
                    Character isClientCall = adEncourage.getIsClientCall();  //是否被客户端回调
                    Character isServerCall = adEncourage.getIsServerCall();  //是否被服务端回调
                    String isSeeEnd = adEncourage.getIsSeeEnd();  //是否看完

                    Long dayId = dayBehaveRecordMapper.getDayLastDayBehaveRecordlistByPlayerId(playerDTO.getPlayerId()).getDayId();//获取用户的该日留存记录id
//计算传输红包值
                    Double encourageEcpm = adEncourage.getEncourageEcpm();
                    double rewardMaxVal = st.Reward_Limit();
                    double userAdvPercentage = st.ADV_Percent();

                    try {
                        adEncourageMapper.changeAdEncourageRecordClose(advID, clickCount, exceptionMsg);

                        if (isServerCall == '1') {//计数无服务端回调的数量 ,不分类型
                            dayBehaveRecordMapper.changeDayBehaveRecordNoCallbackCount(dayId, 1);
                        }
                    } catch (Exception e) {
                        SpringRollBackUtil.rollBack();
                        log.error("修改激励广告关闭状态失败，请联系管理员");
                        throw new AppException(ResponseEnum.UPDATE_AD_ENCOURAGE_CLOSE_FAILED);
                    }
                    String isCloseEncourageAdv = adEncourageMapper.isCloseAdEncourage(advID);//是否关闭广告
                    rewardCount = this.judgeWard(isClientCall, isServerCall, isSeeEnd, isCloseEncourageAdv);//该advID满足几个条件了
                    withdrawCount = this.judgeWithDraw(isClientCall, isSeeEnd, isCloseEncourageAdv);
                    if (encourageEcpm >= rewardMaxVal) {
                        log.info("激励广告ecpm以达到最大奖励上限,ecpm:" + encourageEcpm);
                        encourageEcpm = rewardMaxVal;  //大于的ecpm都按 最大奖励上限来算
                    }

                    // 该次的广告是提现,然后已看激励,需要告知前端跳转去提现
                    String s = rur.get(playerDTO.getPlayerId() + "withdraw");
                    if (s != null && withdrawCount == 3) {
                        // 提现广告默认无奖励的
                        dayBehaveRecordMapper.changeDayBehaveRecordDefaultNoneRewardCount(dayId, 1);
                        rur.set(playerDTO.getPlayerId() + "withdraw", "88");
                        return new ResultMessage(ResponseEnum.WITHDRAW_PRE_WATCH, null);
                    }
                    try {//正常激励的

                        if (rewardCount == 4) //满足奖励条件了
                        {
                            redValue = BigDecimal.valueOf(encourageEcpm).multiply(BigDecimal.valueOf(userAdvPercentage)).multiply(Percentage).divide(BigDecimal.valueOf(10));
//                        redVal = (encourageEcpm * userAdvPercentage * 0.01) / 10;
                            adEncourageMapper.changeAdEncourageRecordReward(advID, redValue);
//                        该玩家的当日服务端回调发放奖励次数,红包总数,当日红包总数   <|>    玩家的余额 的更新
                            dayBehaveRecordMapper.changeDayBehaveRecordCallbackRewardCount(dayId, 1, redValue, redValue);
                            playerMapper.updatePlayerInRed(playerDTO.getPlayerId(), redValue);

                            log.info(advID + "激励广告正常关闭，广告奖励值已入库,该玩家行为数据,当前红包余额都已改变");
                            break;
                        } else {
                            //未满足奖励条件的,记录未发放奖励数量
                            if (isServerCall == '0') {
                                dayBehaveRecordMapper.changeDayBehaveRecordNoRewardCount(dayId, 1);
                            }
                        }
                    } catch (AppException e) {
                        SpringRollBackUtil.rollBack();
                        log.error("修改广告奖励时出错,请联系管理员");
                        throw new AppException(ResponseEnum.UPDATE_AD_ENCOURAGE_REWARD_FAILED);
                    }
                    log.info("激励广告正常关闭,未发放" + advId + "广告奖励...");
                    break;
                }
            }
        }
        return new ResultMessage(ResponseEnum.SUCCESS, redValue);
    }


    /***
     * 判断是否需要发放奖励
     * @param isClientCall 是否客户端回调
     * @param isServerCall 是否服务端回调
     * @param isSeeEnd  是否看完
     * @param isCloseEncourageAdv 是否关闭了广告
     * @return
     */
    public Integer judgeWard(Character isClientCall, Character isServerCall, String isSeeEnd, String isCloseEncourageAdv) {
        Integer a = 0;

        if (isClientCall == '0') {
            a++;
        }
        if (isServerCall == '0') {
            a++;
        }
        if (isSeeEnd.equals("0")) {
            a++;
        }
        if (isCloseEncourageAdv.equals("0")) {
            a++;
        }
        return a;
    }

    /***
     * 判断是否需要满足提现的条件
     * @param isClientCall 是否客户端回调
     * @param isSeeEnd  是否看完
     * @param isCloseEncourageAdv 是否关闭了广告
     * @return
     */
    public Integer judgeWithDraw(Character isClientCall, String isSeeEnd, String isCloseEncourageAdv) {
        Integer a = 0;
        if (isClientCall == '0') {
            a++;
        }
        if (isSeeEnd.equals("0")) {
            a++;
        }
        if (isCloseEncourageAdv.equals("0")) {
            a++;
        }
        return a;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void getWardEncourage(String advEncourageId, String getWardTimeDate) {

        try {
            adEncourageMapper.changeAdEncourageRecordGetWard(advEncourageId, getWardTimeDate);
        } catch (Exception e) {
            SpringRollBackUtil.rollBack();
            log.error("修改激励广告收下奖励状态失败，请联系管理员");
            throw new AppException(ResponseEnum.UPDATE_AD_ENCOURAGE_GET_WARD_FAILED);
        }
        log.info("id为" + advEncourageId + "的激励广告已领取，收下奖励步骤,已记录到数据库");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long clickCountAndFirstClickTime(String advId) {
        String click_key = advId + "click";
        String advType_key = advId + "advType";
        String typeName = "";
        Long l = 0L;
        String advType = rur.get(advType_key);
//        请求一次 ，redis中将该广告点击次数+1
        if (rur.get(click_key) == null) {
            try {
                l = rur.incrBy(click_key, 1);
                switch (advType) {
                    case "1": { //开屏
                        adOpenscreenMapper.openScreenFirstClickTime(advId, LocalDateTime.now());
                        typeName = "开屏";
                        break;
                    }
                    case "2": {//插屏
                        adInscreenMapper.inScreenFirstClickTime(advId, LocalDateTime.now());
                        typeName = "插屏";
                        break;
                    }
                    case "3": {//信息流
                        adStreamMapper.streamFirstClickTime(advId, LocalDateTime.now());
                        typeName = "信息流";
                        break;
                    }
                    case "4": {//横幅
                        adRowstyleMapper.rowStyleFirstClickTime(advId, LocalDateTime.now());
                        typeName = "横幅";
                        break;
                    }
                    case "5": { //激励广告
                        adEncourageMapper.changeAdEncourageRecordClickTime(advId, LocalDateTime.now(), 1);
                        typeName = "激励";
                        break;
                    }
                }
            } catch (Exception e) {
                SpringRollBackUtil.rollBack();
                log.error(typeName + "广告的首次点击时间记录失败，请联系管理员");
                throw new AppException(ResponseEnum.RECORDIING_AD_ENCOURAGE_FIRST_CLICK_TIME_RECORD_FAILED);
            }
            log.info(typeName + "广告,首次点击已记入redis,首次点击时间已入库");
        }
//        Integer clicks = Integer.parseInt(rur.get(click_key)); //redis中的点击次数

//        if (clicks < 4) {
//
//            try {
//                Long advID = Long.parseLong(advId);
//                //点击次数<5就取redis中的点击次数存到数据库中 ,>5就清redis
//                switch (advType) {
//                    case "1": { //开屏
//                        adOpenscreenMapper.changeAdOpenRecord(advID, AdOpenscreen.builder().clicks(clicks).build());
//                        typeName = "开屏";
//                        break;
//                    }
//                    case "2": {//插屏
//                        adInscreenMapper.changeAdInScreenRecord(advID, AdInscreen.builder().clicks(clicks).build());
//                        typeName = "插屏";
//                        break;
//                    }
//                    case "3": {//信息流
//                        adStreamMapper.changeAdStreamRecord(advID, AdStream.builder().clicks(clicks).build());
//                        typeName = "信息流";
//                        break;
//                    }
//                    case "4": {//横幅
//                        adRowstyleMapper.changeAdRowRecord(advID, AdRowstyle.builder().clicks(clicks).build());
//                        typeName = "横幅";
//                        break;
//                    }
//                    case "5": {//激励广告
//                        LocalDateTime firstClickTime = adEncourageMapper.selectByPrimaryKey(advID).getFirstClickTime();
//                        adEncourageMapper.changeAdEncourageRecordClickTime(advId, firstClickTime, clicks);
//                        typeName = "激励";
//                        break;
//                    }
//                }
//            } catch (AppException e) {
//                SpringRollBackUtil.rollBack();
//                log.error("存储广告点击量和清理redisKEY失败,请检查--> clickCountAndFirstClickTime <--");
//                throw new AppException(ResponseEnum.STORING_AD_CLICK_AND_DELETE_REDIS_KEY_FAILED);
//            }
//            log.info("广告id为" + advId + "的" + typeName + "广告点击次数已计入库");
//        }
//        if (clicks >= 5) {
//            //清掉redis中关联的key
//            rur.delete(click_key);
//            rur.delete(advType_key);
//            log.warn("广告id为" + advId + "的" + typeName + "广告点击次数已达上限");
//            return -1L;
//        }
        l = rur.incrBy(click_key, 1);//redis中点击次数自增
        return l;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void isOKchangeAdEncourageRecord(Long advEncourageId) {

        try {
            adEncourageMapper.changeAdEncourageRecordIsOk(advEncourageId, "0");
            log.info("完成观看激励 步骤成功");
        } catch (Exception e) {
            SpringRollBackUtil.rollBack();
            log.error("激励广告完成观看状态修改失败");
            throw new AppException(ResponseEnum.CHANGE_AD_ENCOURAGE_RECORD_FAILED);
        }

    }


}
