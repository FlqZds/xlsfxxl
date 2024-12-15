package com.yunting.clientservice;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson2.JSONObject;
import com.yunting.client.DTO.*;
import com.yunting.client.common.exception.AppException;
import com.yunting.client.common.results.CallbackMessage;
import com.yunting.client.common.results.ResponseEnum;
import com.yunting.client.common.utils.IpUtils;
import com.yunting.client.common.utils.RedisUtil_Record;
import com.yunting.client.common.utils.SpringRollBackUtil;
import com.yunting.client.entity.Adv.*;
import com.yunting.client.entity.Player;
import com.yunting.client.entity.setting.GameSetting;
import com.yunting.client.entity.setting.UserRewardSetting;
import com.yunting.client.mapper.Adv.*;
import com.yunting.client.mapper.Client.PlayerMapper;
import com.yunting.client.mapper.DayBehaveRecordlistMapper;
import com.yunting.client.mapper.GameSettingMapper;
import com.yunting.clientservice.service.RecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.yunting.client.common.utils.FS.percentage;

@Slf4j
@Service("RecordImpl")
public class RecordImpl implements RecordService {

    @Resource(name = "PlayerMapper")
    private PlayerMapper playerMapper;

    @Resource(name = "RedisUtil_Record")
    private RedisUtil_Record rur;

    @Resource(name = "AdRowstyleMapper")
    private AdRowstyleMapper adRowstyleMapper;


    @Transactional
    public Long SaveRowStyleRecord(HttpServletRequest request, PlayerDTO playerDTO, AdDto adDto) {

        String ipAddr = IpUtils.getIpAddr(request);  //ip

        Long playerId = playerDTO.getPlayerId();

        Player player = playerMapper.selectPlayerByPlayerId(playerId);

        LocalDateTime requestTime = LocalDateTime.parse(adDto.getRequestTime());

        AdRowstyle rowstyle = AdRowstyle.builder().advTypeId('4')
                .playerId(playerId)
                .appId(playerDTO.getGameId().toString())
                .codeBitId(adDto.getCodeBitId())
                .requestId(adDto.getRequestId())
                .requestTime(requestTime)
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
            log.info("添加横幅广告，广告ID：{}", rowstyle.getAdRowstyleId());
            rur.set(rowstyle.getAdRowstyleId() + "advType", rowstyle.getAdvTypeId() + "");  //存 这个广告的类型
            return rowstyle.getAdRowstyleId();
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

        LocalDateTime requestTime = LocalDateTime.parse(adDto.getRequestTime());

        AdInscreen adInscreen = AdInscreen.builder().advTypeId('2')
                .playerId(playerId)
                .appId(playerDTO.getGameId().toString())
                .codeBitId(adDto.getCodeBitId())
                .requestId(adDto.getRequestId())
                .requestTime(requestTime)
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

        LocalDateTime requestTime = LocalDateTime.parse(adDto.getRequestTime());

        AdOpenscreen adOpenscreen = AdOpenscreen.builder().advTypeId('1')
                .playerId(playerId)
                .appId(playerDTO.getGameId().toString())
                .codeBitId(adDto.getCodeBitId())
                .requestId(adDto.getRequestId())
                .requestTime(requestTime)
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

        LocalDateTime requestTime = LocalDateTime.parse(adDto.getRequestTime());

        Player player = playerMapper.selectPlayerByPlayerId(playerId);

        AdStream adStream = AdStream.builder().advTypeId('3')
                .playerId(playerId)
                .appId(playerDTO.getGameId().toString())
                .codeBitId(adDto.getCodeBitId())
                .requestId(adDto.getRequestId())
                .requestTime(requestTime)
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
    @Value("${reward_name}")
    private String reward_name;
    @Value("${reward_amount}")
    private String reward_amount;

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

        //  sha256 签名
        String sign_data = appSecurityKey + ":" + trans_id;

        String sign_raw = DigestUtil.sha256Hex(sign_data);

        if (sign.equals(sign_raw) == false) {
            log.error("激励广告回传签名不匹配，请联系管理员");
            return new CallbackMessage(false, 50001);
        }

        try {
            adEncourageMapper.changeAdEncourageRecordServcer(adencourageId, trans_id, LocalDateTime.now());
            Long dayId = dayBehaveRecordMapper.getDayLastDayBehaveRecordlistByPlayerId(adEncourage.getPlayerId()).getDayId();
            dayBehaveRecordMapper.changeDayBehaveRecordCallbackCount(dayId, 1);
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

        try {
            String clientTime = LocalDateTime.now().toString();
            Integer integer = adEncourageMapper.changeAdEncourageRecordEnoughReward(advEncourageId, clientTime, trasnId);

            Long playerId = playerDTO.getPlayerId();

            log.info("达到奖励条件修改激励广告的记录,达到奖励条件 步骤成功");
        } catch (Exception e) {
            SpringRollBackUtil.rollBack();
            log.error("激励广告达到奖励条件修改失败，请联系管理员");
            throw new AppException(ResponseEnum.UPDATE_AD_ENCOURAGE_ENOUGH_FAILED);
        }
    }

    @Resource(name = "DayBehaveRecordlistMapper")
    private DayBehaveRecordlistMapper dayBehaveRecordMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BigDecimal closeRecordingClick(PlayerDTO playerDTO, String advId, String packageName, Integer clickCount, String exceptionMsg) {

        String advType = rur.get(advId + "advType");

        Long advID = Long.parseLong(advId);

        BigDecimal redValue = new BigDecimal(-1);//返回的红包值 -1代表不是激励的红包值
        switch (advType) {
            case "1": //开屏广告
            {
                try {
                    AdOpenscreen adOpenscreen = AdOpenscreen.builder().clicks(clickCount).build();
                    Long adOpenRecordID = changeAdOpenscreenRecord(advID, adOpenscreen);
                    rur.delete(advType);
                } catch (Exception e) {
                    SpringRollBackUtil.rollBack();
                    log.error("修改开屏广告关闭状态失败，请联系管理员");
                    throw new AppException(ResponseEnum.UPDATE_AD_OPEN_CLOSE_FAILED);
                }
                log.info("<修改开屏广告关闭>操作成功");
                break;
            }
            case "2": //插屏广告
            {
                try {
                    AdInscreen adInscreen = AdInscreen.builder().clicks(clickCount).build();
                    Long inscreenRecordID = changeAdInscreenRecord(advID, adInscreen);
                    rur.delete(advType);
                } catch (Exception e) {
                    SpringRollBackUtil.rollBack();
                    log.error("修改插屏广告关闭状态失败，请联系管理员");
                    throw new AppException(ResponseEnum.UPDATE_AD_IN_CLOSE_FAILED);
                }
                log.info("<修改插屏广告关闭>操作成功");
                break;
            }
            case "3": //信息流广告
            {
                try {
                    AdStream adStream = AdStream.builder().clicks(clickCount).build();
                    Long adStreamRecordID = changeAdStreamRecord(advID, adStream);
                    rur.delete(advType);
                } catch (Exception e) {
                    SpringRollBackUtil.rollBack();
                    log.error("修改信息流广告关闭状态失败，请联系管理员");
                    throw new AppException(ResponseEnum.UPDATE_AD_STREAM_CLOSE_FAILED);
                }
                log.info("<信修改息流广告关闭>操作录成功");
                break;
            }
            case "4": //横幅广告
            {
                try {
                    AdRowstyle adRowstyle = AdRowstyle.builder().clicks(clickCount).build();
                    Long adRowRecordID = changeAdRowRecord(advID, adRowstyle);
                    rur.delete(advType);
                } catch (Exception e) {
                    SpringRollBackUtil.rollBack();
                    log.error("修改横幅广告关闭状态失败，请联系管理员");
                    throw new AppException(ResponseEnum.UPDATE_AD_ROWSTYLE_CLOSE_FAILED);
                }
                log.info("<修改横幅广告关闭>操作成功");
                break;
            }
            case "5": //激励广告
            {
                Integer rewardCount = 0;
                AdEncourage adEncourage = adEncourageMapper.selectByPrimaryKey(advID);

//                判断是否给用户奖励
                Character isClientCall = adEncourage.getIsClientCall();  //是否被客户端回调
                Character isServerCall = adEncourage.getIsServerCall();  //是否被服务端回调
                String isSeeEnd = adEncourage.getIsSeeEnd();  //是否看完
//                计算传输红包值
                Double encourageEcpm = adEncourage.getEncourageEcpm();
                GameSetting gameSetting = gameSettingMapper.getGameSettingByPackageName(packageName);
                UserRewardSetting rewardSetting = gameSettingMapper.getUserRewardSetting(gameSetting.getUserRewardSettingId());
                Double rewardMaxVal = Double.valueOf(rewardSetting.getGetRewardMaxVal());
                Double userAdvPercentage = rewardSetting.getUserAdvPercentage();

                try {
                    adEncourageMapper.changeAdEncourageRecordClose(advID, clickCount, exceptionMsg);
                } catch (Exception e) {
                    SpringRollBackUtil.rollBack();
                    log.error("修改激励广告关闭状态失败，请联系管理员");
                    throw new AppException(ResponseEnum.UPDATE_AD_ENCOURAGE_CLOSE_FAILED);
                }
                String isCloseEncourageAdv = adEncourageMapper.isCloseAdEncourage(advID);//是否关闭广告
                rewardCount = this.judgeWard(isClientCall, isServerCall, isSeeEnd, isCloseEncourageAdv);//该advID满足几个条件了
                if (encourageEcpm >= rewardMaxVal) {
                    log.info("激励广告ecpm以达到最大奖励上限,ecpm:" + encourageEcpm);
                    encourageEcpm = rewardMaxVal;  //大于的ecpm都按 最大奖励上限来算
                }

                try {
                    // 该次的广告是提现,然后已看激励,需要告知前端跳转去提现
                    String s = rur.get("withdraw" + playerDTO.getPlayerId());
                    if (s != null && rewardCount > 3) {
                        rur.set("withdraw" + playerDTO.getPlayerId(), "88");
                        throw new AppException(ResponseEnum.WITHDRAW_PRE_WATCH);
                    }

                    if (rewardCount == 4) //满足奖励条件了
                    {
                        redValue = BigDecimal.valueOf(encourageEcpm).multiply(BigDecimal.valueOf(userAdvPercentage)).multiply(percentage).divide(BigDecimal.valueOf(10));
//                        redVal = (encourageEcpm * userAdvPercentage * 0.01) / 10;
                        adEncourageMapper.changeAdEncourageRecordReward(advID, redValue);
//                        该玩家的当日服务端回调发放奖励次数,红包总数,当日红包总数   <|>    玩家的余额 的更新
                        Long dayId = dayBehaveRecordMapper.getDayLastDayBehaveRecordlistByPlayerId(playerDTO.getPlayerId()).getDayId();
                        dayBehaveRecordMapper.changeDayBehaveRecordCallbackRewardCount(dayId, 1, redValue, redValue);
                        playerMapper.updatePlayerInRed(playerDTO.getPlayerId(), redValue);

                        rur.delete(advId + "advType");
                        log.info(advID + "激励广告关闭，广告奖励值已入库,该玩家行为数据,当前红包余额都已改变");
                        return redValue;
                    }
                } catch (Exception e) {
                    SpringRollBackUtil.rollBack();
                    log.error("修改广告奖励时出错,请联系管理员");
                    throw new AppException(ResponseEnum.UPDATE_AD_ENCOURAGE_REWARD_FAILED);
                }
                log.info("激励广告关闭,未发放" + advId + "广告奖励...");
                break;
            }
        }
//清掉redis中关联的key
        rur.delete(advId + "advType");
        log.info("<修改广告关闭>操作成功");
        return redValue;
    }

    @Resource(name = "GameSettingMapper")
    private GameSettingMapper gameSettingMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void compensateClick(String advId, Integer clickCount) {
        String advType = rur.get(advId + "advType");

        Long advID = Long.parseLong(advId);

        switch (advType) {
            case "1": //开屏广告
            {
                try {
                    AdOpenscreen adOpenscreen = AdOpenscreen.builder().clicks(clickCount).build();
                    changeAdOpenscreenRecord(advID, adOpenscreen);
                    rur.delete(advType);
                } catch (Exception e) {
                    SpringRollBackUtil.rollBack();
                    log.error("补发开屏广告点击量失败，请联系管理员");
                    throw new AppException(ResponseEnum.AD_CLICK_COMPENSATE_FAILED);
                }
                log.info("<补发开屏广告点击量>操作成功");
                break;
            }
            case "2": //插屏广告
            {
                try {
                    AdInscreen adInscreen = AdInscreen.builder().clicks(clickCount).build();
                    changeAdInscreenRecord(advID, adInscreen);
                    rur.delete(advType);
                } catch (Exception e) {
                    SpringRollBackUtil.rollBack();
                    log.error("补发插屏广告点击量失败，请联系管理员");
                    throw new AppException(ResponseEnum.AD_CLICK_COMPENSATE_FAILED);
                }
                log.info("<补发插屏广告点击量>操作成功");
                break;
            }
            case "3": //信息流广告
            {
                try {
                    AdStream adStream = AdStream.builder().clicks(clickCount).build();
                    changeAdStreamRecord(advID, adStream);
                    rur.delete(advType);
                } catch (Exception e) {
                    SpringRollBackUtil.rollBack();
                    log.error("补发信息流广告点击量失败，请联系管理员");
                    throw new AppException(ResponseEnum.AD_CLICK_COMPENSATE_FAILED);
                }
                log.info("<补发信息流广告点击量>操作录成功");
                break;
            }
            case "4": //横幅广告
            {
                try {
                    AdRowstyle adRowstyle = AdRowstyle.builder().clicks(clickCount).build();
                    changeAdRowRecord(advID, adRowstyle);
                    rur.delete(advType);
                } catch (Exception e) {
                    SpringRollBackUtil.rollBack();
                    log.error("补发横幅广告点击量失败，请联系管理员");
                    throw new AppException(ResponseEnum.AD_CLICK_COMPENSATE_FAILED);
                }
                log.info("<补发横幅广告点击量>操作成功");
                break;
            }
            case "5": //横幅广告
            {
                try {
                    adEncourageMapper.compensateAdEncourageClickCount(advID, clickCount);
                    rur.delete(advType);
                } catch (Exception e) {
                    SpringRollBackUtil.rollBack();
                    log.error("补发横幅广告点击量失败，请联系管理员");
                    throw new AppException(ResponseEnum.AD_CLICK_COMPENSATE_FAILED);
                }
                log.info("<补发横幅广告点击量>操作成功");
                break;
            }
        }
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
//        请求一次 ，redis中将该广告次点击次数+1
        if (rur.get(click_key) == null) {
            try {
                l = rur.incrBy(click_key, 1);
                switch (advType) {
                    case "1": { //开屏
                        adOpenscreenMapper.openScreenFirstClickTime(advId, LocalDateTime.now());
                        typeName = "开屏";
                        break;
                    }
                    case "2": //插屏
                        adInscreenMapper.inScreenFirstClickTime(advId, LocalDateTime.now());
                        typeName = "插屏";
                        break;
                    case "3": //信息流
                        adStreamMapper.streamFirstClickTime(advId, LocalDateTime.now());
                        typeName = "信息流";
                        break;
                    case "4": //横幅
                        adRowstyleMapper.rowStyleFirstClickTime(advId, LocalDateTime.now());
                        typeName = "横幅";
                        break;
                    case "5": //激励广告
                        adEncourageMapper.changeAdEncourageRecordClickTime(advId, LocalDateTime.now(), 1);
                        typeName = "激励";
                        break;
                }
            } catch (Exception e) {
                SpringRollBackUtil.rollBack();
                log.error(typeName + "广告的首次点击时间记录失败，请联系管理员");
                throw new AppException(ResponseEnum.RECORDIING_AD_ENCOURAGE_FIRST_CLICK_TIME_RECORD_FAILED);
            }
            log.info(typeName + "广告,首次点击已记入redis,首次点击时间已入库");
        }
        Integer clicks = Integer.parseInt(rur.get(click_key)); //redis中的点击次数

        if (clicks < 4) {

            try {
                Long advID = Long.parseLong(advId);
                //点击次数<5就取redis中的点击次数存到数据库中 ,>5就清redis
                switch (advType) {
                    case "1": { //开屏
                        changeAdOpenscreenRecord(advID, AdOpenscreen.builder().clicks(clicks).build());
                        typeName = "开屏";
                        break;
                    }
                    case "2": //插屏
                        changeAdInscreenRecord(advID, AdInscreen.builder().clicks(clicks).build());
                        typeName = "插屏";
                        break;
                    case "3": //信息流
                        changeAdStreamRecord(advID, AdStream.builder().clicks(clicks).build());
                        typeName = "信息流";
                        break;
                    case "4": //横幅
                        changeAdRowRecord(advID, AdRowstyle.builder().clicks(clicks).build());
                        typeName = "横幅";
                        break;
                    case "5": //激励广告
                        LocalDateTime firstClickTime = adEncourageMapper.selectByPrimaryKey(advID).getFirstClickTime();
                        adEncourageMapper.changeAdEncourageRecordClickTime(advId, firstClickTime, clicks);
                        typeName = "激励";
                        break;
                }
            } catch (Exception e) {
                SpringRollBackUtil.rollBack();
                log.error("存储广告点击量和清理redisKEY失败,请检查--> clickCountAndFirstClickTime <--");
                throw new AppException(ResponseEnum.STORING_AD_CLICK_AND_DELETE_REDIS_KEY_FAILED);
            }
            log.info("广告id为" + advId + "的" + typeName + "广告点击次数已计入库");
        }


        if (clicks >= 5) {
            //清掉redis中关联的key
            rur.delete(click_key);
            rur.delete(advType_key);
            log.warn("广告id为" + advId + "的" + typeName + "广告点击次数已达上限");
            return -1L;
        }
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


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long changeAdRowRecord(Long advEncourageId, AdRowstyle adRowstyle) {
        try {
            Integer integer = adRowstyleMapper.changeAdRowRecord(advEncourageId, adRowstyle);
            if (integer != 1) {
                log.error("更改横幅广告失败，请联系管理员");
                throw new AppException(ResponseEnum.CHANGE_AD_ROWSTYLE_FAILED);
            }
            Long adRowstyleId = adRowstyle.getAdRowstyleId();
            log.info("横幅广告记录更新成功,ID为：" + adRowstyleId);
            return adRowstyleId;
        } catch (Exception e) {
            e.printStackTrace();
            SpringRollBackUtil.rollBack();
            throw new AppException(ResponseEnum.CHANGE_AD_ROWSTYLE_FAILED);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long changeAdInscreenRecord(Long advInscreenId, AdInscreen adInscreen) {
        try {
            Integer integer = adInscreenMapper.changeAdInScreenRecord(advInscreenId, adInscreen);
            if (integer != 1) {
                log.error("更改插屏广告失败，请联系管理员");
                throw new AppException(ResponseEnum.CHANGE_AD_INSCREEN_FAILED);
            }
            log.info("插屏广告记录更新成功,ID为：" + advInscreenId);
            return advInscreenId;
        } catch (Exception e) {
            e.printStackTrace();
            SpringRollBackUtil.rollBack();
            throw new AppException(ResponseEnum.CHANGE_AD_INSCREEN_FAILED);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long changeAdOpenscreenRecord(Long advEncourageId, AdOpenscreen adOpenscreen) {
        try {
            Integer integer = adOpenscreenMapper.changeAdOpenRecord(advEncourageId, adOpenscreen);
            if (integer != 1) {
                log.error("更改开屏广告失败，请联系管理员");
                throw new AppException(ResponseEnum.CHANGE_AD_OPENSCREEN_FAILED);
            }
            log.info("开屏广告记录更新成功,ID为：" + advEncourageId);
            return advEncourageId;
        } catch (Exception e) {
            e.printStackTrace();
            SpringRollBackUtil.rollBack();
            throw new AppException(ResponseEnum.CHANGE_AD_OPENSCREEN_FAILED);
        }
    }

    @Override
    @Transactional
    public Long changeAdStreamRecord(Long adStreamId, AdStream adStream) {
        try {
            Integer integer = adStreamMapper.changeAdStreamRecord(adStreamId, adStream);
            if (integer != 1) {
                log.error("更改信息流广告失败，请联系管理员");
                throw new AppException(ResponseEnum.CHANGE_AD_STREAM_FAILED);
            }
            log.info("信息流广告记录更新成功,ID为：" + adStreamId);
            return adStreamId;
        } catch (Exception e) {
            e.printStackTrace();
            SpringRollBackUtil.rollBack();
            throw new AppException(ResponseEnum.CHANGE_AD_STREAM_FAILED);
        }
    }


}
