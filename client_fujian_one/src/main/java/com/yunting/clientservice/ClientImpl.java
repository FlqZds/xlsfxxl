package com.yunting.clientservice;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alipay.api.domain.Participant;
import com.yunting.client.DTO.PlayerDTO;
import com.yunting.client.DTO.VO.DeviceDTO;
import com.yunting.client.DTO.VO.GameSettingVo;
import com.yunting.client.DTO.VO.infoVO;
import com.yunting.client.DTO.dataTransFer.PlayerMetaData;
import com.yunting.client.DTO.dataTransFer.SignDto;
import com.yunting.client.common.exception.AppException;
import com.yunting.client.common.results.ResponseEnum;
import com.yunting.client.common.results.ResultMessage;
import com.yunting.client.common.utils.*;
import com.yunting.client.entity.Adv.AdEncourage;
import com.yunting.client.entity.*;
import com.yunting.client.entity.setting.*;
import com.yunting.client.mapper.Adv.AdEncourageMapper;
import com.yunting.client.mapper.Client.DeviceRecordlistMapper;
import com.yunting.client.mapper.Client.LocationMapper;
import com.yunting.client.mapper.Client.PlayerMapper;
import com.yunting.client.mapper.Client.WithdrawRecordMapper;
import com.yunting.client.mapper.DayBehaveRecordlistMapper;
import com.yunting.client.mapper.ExceptionRecordlsitMapper;
import com.yunting.client.mapper.GameSettingMapper;
import com.yunting.client.mapper.MobileDetailMapper;
import com.yunting.clientservice.service.ClientService;
import com.yunting.forest.ForestService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.yunting.client.common.utils.AliPayUtil.IdentityType;
import static com.yunting.client.common.utils.FS.*;

@Slf4j
@Service("ClientService")
@Data
public class ClientImpl implements ClientService {
    @Resource(name = "PlayerMapper")
    private PlayerMapper playerMapper;

    @Resource(name = "LocationMapper")//定位表
    private LocationMapper locationMapper;

    @Resource(name = "ForestService")
    private ForestService forest;

    @Resource(name = "JWTutil")
    JWTutil jwTutil;

    @Resource(name = "RedisUtil_Record")
    private RedisUtil_Record rur;

    @Resource(name = "RedisUtils")
    private RedisUtils_Wlan rs;

    @Resource(name = "RedisUtil_session")
    private RedisUtil_session rus;

    @Resource(name = "AdEncourageMapper")
    private AdEncourageMapper adEncourageMapper;

    @Resource(name = "DeviceRecordlistMapper")
    private DeviceRecordlistMapper deviceRecordlistMapper;

    @Resource(name = "ExceptionRecordlsitMapper")
    private ExceptionRecordlsitMapper exceptionRecordlsitMapper;

    @Resource(name = "MobileDetailMapper")
    private MobileDetailMapper mobileDetailMapper;

    @Resource(name = "ForestService")
    private ForestService forestService;

    @Resource(name = "WxRequestUtil")
    private WxRequestUtil wxUtil;

    @Resource(name = "ExceptionRecording")
    private ExceptionRecording exRec;

    @Resource(name = "GatheringUtils")
    private sessionUtils sessionUtils;

    @Resource(name = "WithdrawRecordMapper")
    private WithdrawRecordMapper withdrawMapper;

    @Resource(name = "DayBehaveRecordlistMapper")
    private DayBehaveRecordlistMapper dayBehaveMapper;

    @Resource(name = "AliPayUtil")
    AliPayUtil aliPayUtil;


    /***
     * 添加设备记录
     * <p>
     * (只单纯添加设备记录,没包含去重等任何逻辑)
     * @param dto 玩家的设备信息
     * @param identifiction 玩家的位置
     *
     */
    @Transactional(rollbackFor = Exception.class)
    public void addDeviceRecord(Player player, SignDto dto, String identifiction) {
        Long playerID = player.getPlayerId();
        String wxNickname = player.getWxNickname();

        DeviceRecordlist this_device_record = DeviceRecordlist.builder()
                .playerId(playerID)
                .wxNickname(wxNickname)
                .oaid(dto.getOaid())
                .androidId(dto.getAndroidID())
                .deviceType(dto.getDeviceType())
                .deviceDetail(dto.getDeviceDetail())
                .firstlogintime(LocalDateTime.now())
                .identifiction(identifiction)
                .build();
        try {
            deviceRecordlistMapper.insertDeviceRecord(this_device_record);
        } catch (AppException e) {
            e.printStackTrace();
            SpringRollBackUtil.rollBack();
            log.error("保存设备信息失败，请联系管理员");
            throw new AppException(ResponseEnum.SAVE_DEVICE_RECORD_FAILED);
        }
    }

    /***
     * 更新玩家位置信息
     * <p>
     * (包含新用户没位置信息的插入,老用户的更新和没变更不更新)
     * @param playerDTO 玩家信息
     * @param location 玩家的位置
     *
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateLocation(PlayerDTO playerDTO, String location) {
        String playerID = playerDTO.getPlayerId().toString();
        Location lastLocation = locationMapper.getLastLocationByPlayerId(playerID, LocalDate.now());

        Location location_new = Location.builder()
                .playerId(playerDTO.getPlayerId())
                .location(location)
                .recordTime(LocalDateTime.now())
                .build();

        try { //新用户没有位置信息 和 老用户位置信息变更了
            if (Objects.isNull(lastLocation) || location.equals(lastLocation.getLocation()) == false) {
                locationMapper.insertLocation(location_new);
                log.info("玩家位置信息已变更,现已保存" + location);
            }
        } catch (Exception e) {
            SpringRollBackUtil.rollBack();
            log.error("玩家位置信息更新失败，请联系管理员", new AppException(ResponseEnum.UPDATE_LOCATION_FAILED));
            throw new AppException(ResponseEnum.UPDATE_LOCATION_FAILED);
        }

        if (lastLocation != null) {
            String old_location = lastLocation.getLocation();
            //更新位置信息的同时 ,将redis中该玩家的信息也一并更新
            Object o = rs.hGet(old_location + "AL", playerID);
            if (o != null) {         //同时该玩家原有的位置+序号也需要删掉  (如果该位置有的话)
                String last_location_index = o.toString();
                rs.hDelete(old_location, last_location_index);
                rs.hDelete(old_location + "AL", playerID);

                rs.hPut(location, rs.hSize("location") + "", playerID);
                rs.hPut(location + "AL", playerID, rs.hSize("location") + "");
            }
        }
    }

    /***
     * 生成token的(只单纯生成token,放到playerDTO中)
     * @param player 玩家
     * @return 登录玩家的一些信息
     */
    private PlayerDTO generateToken(Player player) {
        PlayerDTO playerDTO = new PlayerDTO();
        BeanUtils.copyProperties(player, playerDTO);
        String token = jwTutil.generateJwt_master(playerDTO);

        playerDTO.setPlayerToken(token);
        playerDTO.setTokenExpiredTime(LocalDateTime.now().plusHours(3L).atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()); //过期时间转为时间戳

        log.info(player.getWxNickname() + "token已生成");
        return playerDTO;
    }


    /***
     * 免审核提现申请接口
     * <p>
     * 提现订单生成
     * @param playerId  提现的玩家id
     * @param payId  提现的玩家支付宝id
     * @param realName 体现玩家的姓名
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultMessage applyWithdraw(Long playerId, String payId, String realName) {
        ResultMessage resultMessage = new ResultMessage(ResponseEnum.SUCCESS, null);

        String is_see_enc = rur.get("withdraw" + playerId) + "";
        int see_count = Integer.parseInt(is_see_enc);
        if (see_count != 88) {
            log.info("玩家提现之前要先去看一个激励广告", new AppException(ResponseEnum.PLAYER_NO_SEE_ENCOURAGE));
            rur.setEx("withdraw" + playerId, "0", 5, TimeUnit.SECONDS);
            throw new AppException(ResponseEnum.PLAYER_NO_SEE_ENCOURAGE);
        }

        BigDecimal playerRed_Need_reduce = BigDecimal.ZERO;
        Player player = playerMapper.selectPlayerByPlayerId(playerId);
        Long pName = null;
        Long pID = null;

        String bindPayLoginId = player.getPayLoginId();//玩家已绑定的支付宝id
        String bindRealName = player.getRealName();//玩家已绑定的姓名

        pID = playerMapper.selectPlayerByPayID(payId);//查询数据库中是否有人绑定该pay信息
        pName = playerMapper.selectPlayerByRealName(realName);//查询数据库中是否有人绑定该pay信息

        if (bindPayLoginId != null && bindRealName != null) {
            if (playerId.equals(player.getPlayerId()) == false) {//但是玩家id和传过来的不是同一个人
                if (payId.equals(bindPayLoginId) && realName.equals(bindRealName)) {//绑定信息相同
                    log.warn("该玩家已经被绑定,请勿重复绑定", new AppException(ResponseEnum.PLAYER_ALIPAY_ALREADY_BIND));
                    throw new AppException(ResponseEnum.PLAYER_ALIPAY_ALREADY_BIND);
                }
            } else { //是同一个人 ,但是这次传的和数据库中的不一样
                if (payId.equals(bindPayLoginId) == false || realName.equals(bindRealName) == false) {//绑定信息相同
                    log.warn("您所提交的支付宝账号信息与上次有所不同", new AppException(ResponseEnum.ALIPAY_INFO_DIFFERENCE));
                    throw new AppException(ResponseEnum.ALIPAY_INFO_DIFFERENCE);
                }
            }
        }


        BigDecimal playerInRed = player.getInRed(); //用户当前余额

        WithdrawSetting withdrawSetting = withdrawMapper.getWithdrawSetting(player.getGameId());
        String packageName = withdrawSetting.getPackageName();

        DayBehaveRecordlist dayRecord = dayBehaveMapper.getDayLastDayBehaveRecordlistByPlayerId(playerId);
        BigDecimal dayCash = dayRecord.getDayCash(); //用户当日已提现总金额

        BigDecimal withdrawPercentage = new BigDecimal(withdrawSetting.getWithdrawPercentage());//提现比例
        Integer withdrawCount = withdrawSetting.getWithdrawCount();//获取该用户的 当日提现次数上限
        BigDecimal withdrawNojudgeMoney = withdrawSetting.getWithdrawNojudgeMoney(); //设置的免审核金额

        String playerTodayCount = "";
        if (rs.hExists("withdrawCount", playerId + "") == true) {
            playerTodayCount = rs.hGet("withdrawCount", playerId + "").toString();
        } else {
            rs.hPutIfAbsent("withdrawCount", playerId + "", "0");
            playerTodayCount = rs.hGet("withdrawCount", playerId + "").toString();
        }
        long l = Long.parseLong(playerTodayCount);

        if (l >= withdrawCount) {
            log.warn("玩家当日提现次数已超过上限，无法提现", new AppException(ResponseEnum.PLAYER_WITHDRAW_COUNT_OVER_LIMIT));
            throw new AppException(ResponseEnum.PLAYER_WITHDRAW_COUNT_OVER_LIMIT);
        }

        if (playerInRed.compareTo(percentage) == -1) {
            log.warn("用户余额不足,无法提现", new AppException(ResponseEnum.PLAYER_NO_MORE_MONEY));
            throw new AppException(ResponseEnum.PLAYER_NO_MORE_MONEY);
        }

        //实际提现金额
        playerInRed = playerInRed.setScale(2, BigDecimal.ROUND_DOWN);  //省去10012的12
        BigDecimal transAmount = playerInRed.divide(withdrawPercentage);
        transAmount = transAmount.setScale(2, BigDecimal.ROUND_DOWN);
        log.info("要提现的值:" + transAmount);
        if (transAmount.compareTo(MAXRed) != -1) {
            log.error("提现金额超出200.00元,拒绝该次交易", new AppException(ResponseEnum.PLAYER_WITHDRAW_MONEY_TOO_LARGE));
            throw new AppException(ResponseEnum.PLAYER_WITHDRAW_MONEY_TOO_LARGE);
        }

        if (transAmount.compareTo(limitRed) == -1) {
            log.warn("提现金额太小，无法提现", new AppException(ResponseEnum.PLAYER_WITHDRAW_MONEY_TOO_SMALL));
            throw new AppException(ResponseEnum.PLAYER_WITHDRAW_MONEY_TOO_SMALL);
        }

        Integer i = transAmount.compareTo(withdrawNojudgeMoney); //-1 0 1  <=>   是否触发审核
        String limitRebackMoney_str = withdrawMapper.getLimitRebackMoney();
        BigDecimal limitRebackMoney = new BigDecimal(limitRebackMoney_str);//最低返现金额
        Integer j = transAmount.compareTo(limitRebackMoney); //-1 0 1  <=>   是否触发返现

        //实际返现金额
        BigDecimal rebackVal = null;
        if (j != -1) {

            BigDecimal thisRebackPer_end = null;
            BigDecimal thisRebackPer_begin = null;

            if (withdrawMapper.getThisReback(dayCash.add(transAmount)) == null) {
                thisRebackPer_begin = BigDecimal.ZERO;
            } else {
                thisRebackPer_begin = new BigDecimal(withdrawMapper.getThisReback(dayCash.add(transAmount)));//begin返现比例
            }
            if (withdrawMapper.getThisReback(dayCash) == null) {
                thisRebackPer_end = BigDecimal.ZERO;
            } else {
                thisRebackPer_end = new BigDecimal(withdrawMapper.getThisReback(dayCash));//end (已提现全部金额的返现比例)
            }

            thisRebackPer_begin = thisRebackPer_begin.multiply(percentage);
            thisRebackPer_end = thisRebackPer_end.multiply(percentage);

            BigDecimal toReduce = dayCash.multiply(thisRebackPer_end);  //计算公式后面要减掉的那一坨
            toReduce = toReduce.setScale(2, BigDecimal.ROUND_DOWN);

            BigDecimal added = dayCash.add(transAmount); //总的+这次的
            added = added.multiply(thisRebackPer_begin);               //计算公式前面那一坨
            added = added.setScale(2, BigDecimal.ROUND_DOWN);


            rebackVal = added.subtract(toReduce);//返现金额
            log.info("提现金额:" + transAmount + "金额触发返现,返现金额为:" + rebackVal
                    + "  该次前面触发的返现比例为:" + thisRebackPer_begin + "该次后面触发的返现比例为:" + thisRebackPer_end);
        } else {
            rebackVal = new BigDecimal("0");
            log.info("提现金额:" + transAmount + "金额未触发返现,最低为:" + limitRebackMoney + "触发");
        }

        WithdrawRecord withdrawRecord = WithdrawRecord.builder()
                .withdrawMoney(transAmount).returnMoney(rebackVal)
                .playerId(playerId).packageName(packageName)
                .withdrawPercentageNow(String.valueOf(withdrawPercentage))
                .withdrawTime(LocalDateTime.now()).wxNickname(player.getWxNickname()).build();

        try {

            if (i != -1) {
                //审核
                if (player.getRealName() == null || player.getPayLoginId() == null) {  //提现成功,绑定该用户支付宝
                    rs.hPutIfAbsent("payInfo", playerId + "payId", payId);
                    rs.hPutIfAbsent("payInfo", playerId + "realName", realName);
                }

                withdrawRecord.setWithdrawStatus('1');
                resultMessage.setMessage(ResponseEnum.WITHDRAW_ORDER_MENTIONED.getMessage());
                resultMessage.setCode(ResponseEnum.WITHDRAW_ORDER_MENTIONED.getCode());
            } else {
                //免审核的提现
                String OutBizNo = Pay_Order_Header + UUID.randomUUID().toString().replace("-", "");//订单流水号
                String title = "提现成功! 乐益消消乐祝您龙年行大运,完事皆如意";
                String remark = "";
                Participant alipayaccount = new Participant();
                alipayaccount.setIdentityType(IdentityType);
                alipayaccount.setIdentity(payId);
                alipayaccount.setName(realName);
                transAmount = transAmount.add(rebackVal); //返现 提现金额一起发
                BigDecimal cash = BigDecimal.ZERO;
                cash = transAmount.setScale(2, BigDecimal.ROUND_DOWN);

                withdrawRecord.setWithdrawStatus('0');      //该订单为免审核-通过
                playerRed_Need_reduce = (transAmount.subtract(rebackVal));
                dayBehaveMapper.changeDayBehaveRecordWithdrawCash(dayRecord.getDayId(), playerRed_Need_reduce);  //当日提现金额累加

                aliPayUtil.pay(OutBizNo, cash, title, remark, alipayaccount);

                if (bindRealName == null || bindPayLoginId == null) {  //提现成功,绑定该用户支付宝
                    playerMapper.refreshPlayerRealInfo(playerId, payId, realName);
                    log.info("玩家免审核提现成功,绑定该用户支付宝");
                }

                resultMessage.setMessage(ResponseEnum.NO_JUDGE_ORDER_SUCCESSFUL.getMessage());
                resultMessage.setCode(ResponseEnum.NO_JUDGE_ORDER_SUCCESSFUL.getCode());
            }
            withdrawMapper.insertWithdrawRecord(withdrawRecord);              //插入提现记录

            rur.delete("withdraw" + playerId);
            playerMapper.updatePlayerInRed(playerId, playerRed_Need_reduce.multiply(withdrawPercentage).negate());  //玩家余额减少
            rs.hIncrBy("withdrawCount", playerId + "", 1);           //玩家当日提现次数+1
            BigDecimal redWithDrew = playerMapper.selectInRedByPlayerId(playerId, player.getGameId());  //提现后余额
            resultMessage.setData(redWithDrew);
        } catch (AppException e) {
            SpringRollBackUtil.rollBack();
            log.error("玩家免审核提现失败,错误信息:" + e, new AppException(ResponseEnum.PLAYER_WITHDRAW_FAILED));
            resultMessage.setMessage(e.getMessage());
            resultMessage.setCode(e.getCode());
            return resultMessage;
        }
        return resultMessage;
    }

    /***
     * 绑定支付宝
     * playerId 认证的玩家id
     * payId 支付宝用户id
     * realName 姓名
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer manIdentify(Long playerId, String payId, String realName) {
        Long pName = playerMapper.selectPlayerByPayID(payId);
        Long pID = playerMapper.selectPlayerByRealName(realName);
        if (pName != null || pID != null) {
            log.warn("该支付宝账号已被其他玩家绑定", new AppException(ResponseEnum.PLAYER_ALIPAY_ALREADY_BIND));
            throw new AppException(ResponseEnum.PLAYER_ALIPAY_ALREADY_BIND);
        }
        int i = playerMapper.refreshPlayerRealInfo(playerId, payId, realName);
        return i;
    }


    /***
     * 手动添加添加设备信息接口
     * mobileDetail 设备信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void addMobile(MobileDetail mobileDetail) {


        //        如果有 ,就指定该玩家的mobileID
        MobileDetail mobile = mobileDetailMapper.selectMobileNameAndBrand(mobileDetail.getMobileCpu(), mobileDetail.getMobileCpuFluency(), mobileDetail.getDeviceDetail(), mobileDetail.getDeviceType());

        if (Objects.nonNull(mobile)) {  //查到了
            Long mobileID = Long.valueOf(mobile.getMobileId());
            if (mobile.getMobileSystem() != mobileDetail.getMobileSystem()) {
                mobileDetailMapper.changeMobileSystem(mobileID, mobileDetail.getMobileSystem());
            }

            return;
        } else   //        如果数据库中没有该型号数据,就新增该型号
        {
            try {
                MobileDetail detail = MobileDetail.builder().mobileCpu(mobileDetail.getMobileCpu()).mobileCpuFluency(mobileDetail.getMobileCpuFluency())
                        .mobileSystem(mobileDetail.getMobileSystem())
                        .deviceDetail(mobileDetail.getDeviceDetail())
                        .deviceType(mobileDetail.getDeviceType()).build();
                mobileDetailMapper.insert(detail);
                log.info("已手动添加一条设备信息数据");
            } catch (Exception e) {
                SpringRollBackUtil.rollBack();
                log.error("手动添加设备信息数据失败......");
                throw new AppException(ResponseEnum.MOBILE_INFO_ADD_FAILED);
            }
        }

    }


    @Override // 获取风控参数
    public RiskControlSetting getRiskControlSetting(String packageName) {
        RiskControlSetting riskControlSetting = gameSettingMapper.getRiskControlSetting(packageName);

        if (Objects.isNull(riskControlSetting)) {
            log.error("获取风控参数失败，请检查包名:" + packageName + "是否正确");
            throw new AppException(ResponseEnum.GET_RISK_CONTROL_SETTING_FAILED);
        }

        log.info("风控参数已返回");
        return riskControlSetting;
    }

    /***
     * 采集并上传设备信息
     * mobileDetail 设备信息
     * @return
     * 如果采集到已存在的设备, 但系统不同, 则更新设备系统,在返回更新后的设备ID
     * <p>
     * <p>
     * 采集到未存在的设备,则新增设备信息,然后返回新增设备ID
     * <p>
     * <p>
     * 如果什么都没有变,那就返回未变化的设备ID
     *
     */
    @Transactional(rollbackFor = Exception.class)
    public Long collectionAndUploadMobileInfo(MobileDetail mobileDetail) {

        MobileDetail mobile = mobileDetailMapper.selectMobileNameAndBrand(mobileDetail.getMobileCpu(), mobileDetail.getMobileCpuFluency(), mobileDetail.getDeviceDetail(), mobileDetail.getDeviceType());

        if (Objects.nonNull(mobile)) {  //查到了
            Long mobileID = Long.valueOf(mobile.getMobileId());
            if (mobile.getMobileSystem() != mobileDetail.getMobileSystem()) {  //其他相同但是设备系统需要更新
                mobileDetailMapper.changeMobileSystem(mobileID, mobileDetail.getMobileSystem());
                log.info("该型号设备系统已变更");
            }
            log.info("设备信息已校验");
            return mobileID;
        } else   //        如果数据库中没有该型号数据,就新增该型号
        {
            try {
                MobileDetail detail = MobileDetail.builder()
                        .mobileCpu(mobileDetail.getMobileCpu()).mobileCpuFluency(mobileDetail.getMobileCpuFluency())
                        .mobileSystem(mobileDetail.getMobileSystem())
                        .deviceDetail(mobileDetail.getDeviceDetail())
                        .deviceType(mobileDetail.getDeviceType()).build();
                mobileDetailMapper.insert(detail);
                log.info("新增一条该型号数据,设备信息已上传");
                return detail.getMobileId();
            } catch (Exception e) {
                SpringRollBackUtil.rollBack();
                log.error("新增设备型号数据常常出错......");
                throw new AppException(ResponseEnum.MOBILE_INFO_ADD_FAILED);
            }
        }

    }


    /***
     * 没有token
     * 1微信登录 | 注册  ☆
     * 2上传设备记录和地址
     * 3由服务端进行验证
     * 4传风控参数
     *
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultMessage PlayerSignOn(SignDto signDto) {
        String packageName = signDto.getPackageName();//包名
        String wxCode = signDto.getWxCode();          //微信code
        String proxyName = signDto.getProxyName();    //代理名称
        String location = signDto.getLocation();    //地址

        String androidID = signDto.getAndroidID();      //安卓id
        String oaid = signDto.getOaid();                //oaid
        String deviceType = signDto.getDeviceType();    //设备品牌
        String deviceDetail = signDto.getDeviceDetail(); //设备型号

        String mobileCpu = signDto.getMobileCpu();          //CPU
        String mobileSystem = signDto.getMobileSystem();    //设备系统
        String mobileCpuFluency = signDto.getMobileCpuFluency(); //CPU频率

        RiskControlSetting riskControlSetting = this.getRiskControlSetting(packageName); //风控参数
        String cityInfo = IpUtils.getCityInfo(IpUtils.getIp());  //地址

//        先判断注册还是登录
        if (StringUtils.isEmpty(packageName) || StringUtils.isEmpty(proxyName) || StringUtils.isEmpty(wxCode)) {
            log.error("包名||微信code||渠道名称为空");
            throw new AppException(ResponseEnum.PARAMETER_NOT_FOUND);
        }

        String openid = wxUtil.getWxOpenID(packageName, wxCode); //获取微信openID

        MobileDetail md = MobileDetail.builder().deviceType(deviceType).deviceDetail(deviceDetail)
                .mobileCpu(mobileCpu).mobileCpuFluency(mobileCpuFluency).mobileSystem(mobileSystem).build();
        Long mobileID = this.collectionAndUploadMobileInfo(md);//存储+去重(设备型号,安卓系统....)+返回该次的设备id

        Player player_isRegistered = playerMapper.selectPlayerByWxOpenId(openid);

        if (Objects.nonNull(player_isRegistered) == true) { //这里是老用户
            Long player_isRegistered_ID = player_isRegistered.getPlayerId();

            String special = player_isRegistered.getSpecial();  //玩家的身份/状态
            Character status = player_isRegistered.getStatus();  //玩家是否封禁
            DeviceRecordlist deviceRecordlist = deviceRecordlistMapper.getLastDeviceRecordByPlayerId(player_isRegistered_ID);

            if (special.equals("1")) { //这里是普通用户 ,普通用户需要验证一下设备

                if (deviceRecordlist.getOaid().equals(oaid) == false) {
                    log.error("登录异常,oaid与设备记录中的oaid不一致，请检查设备是否更换");
                    exRec.uploadException("OAID异常", player_isRegistered_ID + "", "",
                            player_isRegistered.getWxNickname(), deviceType, deviceDetail, androidID, oaid, ResponseEnum.DEVICE_NOT_MATCH + "");
                    log.info("该用户异常设备型号已记录");
                    return new ResultMessage(ResponseEnum.DEVICE_NOT_MATCH, null);
                }

                if (deviceRecordlist.getAndroidId().equals(androidID) == false) {
                    log.error("登录异常,androidId与设备记录中的androidId不一致，请检查设备是否更换");
                    exRec.uploadException("安卓ID异常", player_isRegistered_ID + "", "",
                            player_isRegistered.getWxNickname(), deviceType, deviceDetail, androidID, oaid, ResponseEnum.DEVICE_NOT_MATCH + "");

                    log.info("该异常设备型号已记录");
                    return new ResultMessage(ResponseEnum.DEVICE_NOT_MATCH, null);
                }

                if (status == '0') {
                    log.error("封禁用户，禁止登录");
                    exRec.uploadException("登录封禁", player_isRegistered_ID + "", "",
                            player_isRegistered.getWxNickname(), deviceType, deviceDetail, androidID, oaid, ResponseEnum.BAN_USER_OUT + "");
                    return new ResultMessage(ResponseEnum.BAN_USER_OUT, null);
                }

            }

            //特殊用户跳过判断 ，只用看是否第一次登录
            try {
                if (special.equals("0") == true) {
                    if (Objects.isNull(deviceRecordlist)) {
                        log.info("特殊用户首次登录 - 虽然注册了,但设备记录却没有的情况");
                        this.addDeviceRecord(player_isRegistered, signDto, "0");
                    }

                    if (deviceRecordlist.getOaid().equals(oaid) == false ||
                            deviceRecordlist.getAndroidId().equals(androidID) == false) {
                        log.info("特殊用户设备变更,现已记录");
                        this.addDeviceRecord(player_isRegistered, signDto, "0");
                    }
                    log.info("特殊用户设备未变更,可以正常登录");
                }
            } catch (Exception e) {
                SpringRollBackUtil.rollBack();
                log.error("特殊用户设备记录添加失败，请联系管理员");
                return new ResultMessage(ResponseEnum.DEVICE_RECORD_ADD_Failed, null);
            }

//        登录的话就先验证设备记录, 不一致就记录异常, 一致就返回 玩家信息+风控参数+地址
            PlayerDTO playerDTO = this.generateToken(player_isRegistered);
            this.updateLocation(playerDTO, location);
            playerDTO.setRiskControlSetting(riskControlSetting);
            playerDTO.setAddress(cityInfo);

            playerMapper.updatePlayerMobileID(player_isRegistered_ID, mobileID);
            log.info("玩家设备id已刷新");

            log.info(player_isRegistered.getWxNickname() + "用户已经存在，将前往登录");
            return new ResultMessage(ResponseEnum.SUCCESS, playerDTO);
        } else {

//        注册的话就保存设备记录,新建玩家,然后返回风控参数
            Player wxUser = wxUtil.getWxUser(proxyName, mobileID);/*请求微信拿到用户信息,在这一步表中已经生成新玩家了*/
            try {
                playerMapper.insertPlayer(wxUser);//保存新玩家
                this.addDeviceRecord(wxUser, signDto, "1");//保存新设备记录
                PlayerDTO playerDTO = this.generateToken(wxUser);
                this.updateLocation(playerDTO, location);
                playerDTO.setRiskControlSetting(riskControlSetting);
                playerDTO.setAddress(cityInfo);

                log.info(wxUser.getWxNickname() + "玩家,注册成功");
                return new ResultMessage(ResponseEnum.SUCCESS, playerDTO);
            } catch (Exception e) {
                SpringRollBackUtil.rollBack();
                log.error("注册失败,请检查操作");
                throw new AppException(ResponseEnum.REGISTER_FAILED);
            }
        }
    }


    /***
     * token还有效
     * 1上传设备记录和地址
     * 2由服务端进行设备验证和聚集验证
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultMessage storeDeviceAndLocationThenIdentify(PlayerDTO playerDTO, infoVO infoVO) {
        String androidID = infoVO.getAndroidID();      //安卓id
        String oaid = infoVO.getOaid();                //oaid
        String deviceType = infoVO.getDeviceType();    //设备品牌
        String deviceDetail = infoVO.getDeviceDetail(); //设备型号

        String mobileCpu = infoVO.getMobileCpu();          //CPU
        String mobileSystem = infoVO.getMobileSystem();    //设备系统
        String mobileCpuFluency = infoVO.getMobileCpuFluency(); //CPU频率

        Long playerID = playerDTO.getPlayerId();
        Player player = playerMapper.selectPlayerByPlayerId(playerID);
        String wxNickname = player.getWxNickname();//玩家微信昵称
        Long mobileId = player.getMobileId();   //玩家设备信息id

        String special = player.getSpecial();  //玩家的身份/状态
        Character status = player.getStatus();  //玩家是否封禁

        if (status == '0') {
            log.error("用户被封禁，禁止登录");
            throw new AppException(ResponseEnum.BAN_USER_OUT);
        }

        DeviceRecordlist deviceRecordlist = deviceRecordlistMapper.getLastDeviceRecordByPlayerId(playerID);

//这里是普通用户 ,普通用户需要验证一下设备
        if (special.equals("1")) {

            if (deviceRecordlist.getOaid().equals(oaid) == false) {
                log.error("登录异常,oaid与设备记录中的oaid不一致，请检查设备是否更换");
                exRec.uploadException("OAID异常", player.getPlayerId() + "", "",
                        player.getWxNickname(), deviceType, deviceDetail, androidID, oaid, ResponseEnum.DEVICE_NOT_MATCH + "");

                MobileDetail md = MobileDetail.builder().deviceType(deviceType).deviceDetail(deviceDetail)
                        .mobileCpu(mobileCpu).mobileCpuFluency(mobileCpuFluency).mobileSystem(mobileSystem).build();
                Long mobileID = this.collectionAndUploadMobileInfo(md);//存储+去重(设备型号,安卓系统....)+返回该次设备id
                playerMapper.updatePlayerMobileID(playerID, mobileID);//更新玩家设备id
                log.info("oaid异常的设备型号已记录");
                log.info("oaid异常的玩家设备id已刷新");
                return new ResultMessage(ResponseEnum.DEVICE_NOT_MATCH, null);
            }

            if (deviceRecordlist.getAndroidId().equals(androidID) == false) {
                log.error("登录异常,androidId与设备记录中的androidId不一致，请检查设备是否更换");
                exRec.uploadException("安卓ID异常", playerID + "", "",
                        player.getWxNickname(), deviceType, deviceDetail, androidID, oaid, ResponseEnum.DEVICE_NOT_MATCH + "");

                MobileDetail md = MobileDetail.builder().deviceType(deviceType).deviceDetail(deviceDetail)
                        .mobileCpu(mobileCpu).mobileCpuFluency(mobileCpuFluency).mobileSystem(mobileSystem).build();
                Long mobileID = this.collectionAndUploadMobileInfo(md);//存储+去重(设备型号,安卓系统....)+返回该次设备id
                playerMapper.updatePlayerMobileID(playerID, mobileID);//更新玩家设备id
                log.info("安卓ID异常的设备型号已记录");
                log.info("安卓ID异常的玩家设备id已刷新");
                return new ResultMessage(ResponseEnum.DEVICE_NOT_MATCH, null);
            }

            if (status == '0') {
                log.error("封禁用户，禁止登录");
                exRec.uploadException("登录封禁", player.getPlayerId() + "", "",
                        player.getWxNickname(), deviceType, deviceDetail, androidID, oaid, ResponseEnum.BAN_USER_OUT + "");

                MobileDetail md = MobileDetail.builder().deviceType(deviceType).deviceDetail(deviceDetail)
                        .mobileCpu(mobileCpu).mobileCpuFluency(mobileCpuFluency).mobileSystem(mobileSystem).build();
                Long mobileID = this.collectionAndUploadMobileInfo(md);//存储+去重(设备型号,安卓系统....)+返回该次设备id
                playerMapper.updatePlayerMobileID(playerID, mobileID);//更新玩家设备id
                log.info("封禁用户的设备id已刷新");
                return new ResultMessage(ResponseEnum.BAN_USER_OUT, null);
            }
        }


//特殊用户跳过判断 ，只看是否第一次登录
        try {
            if (special.equals("0") == true) {
                SignDto signDto = SignDto.builder().oaid(oaid).androidID(androidID)
                        .deviceDetail(deviceDetail).deviceType(deviceType).build();
                if (Objects.isNull(deviceRecordlist)) {
                    log.info("特殊用户首次登录 - 虽然注册了,但设备记录却没有的情况");
                    this.addDeviceRecord(player, signDto, "0");
                }

                if (deviceRecordlist.getOaid().equals(oaid) == false ||
                        deviceRecordlist.getAndroidId().equals(androidID) == false) {
                    log.info("特殊用户设备变更,现已记录");
                    this.addDeviceRecord(player, signDto, "0");
                }
                log.info("特殊用户设备未变更,可以正常登录");
            }
        } catch (Exception e) {
            SpringRollBackUtil.rollBack();
            log.error("特殊用户设备记录添加失败，请联系管理员");
            return new ResultMessage(ResponseEnum.DEVICE_RECORD_ADD_Failed, null);
        }
        String location = infoVO.getLocation();
        this.updateLocation(playerDTO, location);

        String Wifi_json = infoVO.getWifi();
        List<String> wlanList = null;
        if (Objects.nonNull(Wifi_json)) {
            wlanList = JSON.parseArray(Wifi_json, String.class);
        }
        String thisMAC = infoVO.getThisMAC();
        Objects.requireNonNull(location, "有token的聚集验证,位置信息不能为空");
        ResultMessage identMSG = this.identification(playerDTO, thisMAC, wlanList, location);
        rus.setEx(playerID + "DET", location, 9, TimeUnit.SECONDS); //设置为已检测
        rus.sAdd("DET", playerID + "");   //加入已检测队列
        return identMSG;
    }


    /***
     * 聚集验证+同型号检测
     * @param playerDTO 新玩家的信息
     * @param thisMAC 新玩家的自身wifi
     * @param wlanList 新玩家的附近wifi
     * @param location 新玩家的位置信息
     * @return
     */
    //同型号验证和聚集验证  location    设备记录   wlanList  位置信息
    @Transactional(rollbackFor = Exception.class)
    public ResultMessage identification(PlayerDTO playerDTO, String thisMAC, List<String> wlanList, String location) {

        String gameId = playerDTO.getGameId().toString();
        Long playerId = playerDTO.getPlayerId();
        String special = playerMapper.selectPlayerPowerAndStatus(playerId + "", gameId).getSpecial();
        if (special.equals("0")) {
            log.info("特殊用户,跳过聚集验证");
            rus.setEx(playerId + "DET", location, 9, TimeUnit.SECONDS); //设置为已检测
            rus.sAdd("DET", playerId + "");   //加入已检测队列
            return new ResultMessage(ResponseEnum.SUCCESS, null);
        }

        String UserGatheringSetting_json = forest.getUserGatheringSetting(gameId);
        ResultMessage resultMessage = JSONObject.parseObject(UserGatheringSetting_json, ResultMessage.class);
        UserGatheringSetting gathering = JSON.to(UserGatheringSetting.class, resultMessage.getData());
        if (Objects.isNull(gathering)) {
            log.error("聚集设置读取失败,请检查" + gameId + "的gameID是否正确", new AppException(ResponseEnum.GET_USER_GATHERING_SETTING_FAILED));
            throw new AppException(ResponseEnum.GET_USER_GATHERING_SETTING_FAILED);
        }

        Integer pop_L = gathering.getGatheringPopulationLarge(); //聚集大范围设置人数上限
        Integer pop_S = gathering.getGatheringPopulationSmall(); //聚集小范围设置人数上限
        Character choice = gathering.getGatheringChoice();
        Integer deviceLimit = gathering.getGatheringDeviceLimit();  //聚集设备数量上限
        Integer sameMacPop = gathering.getSameMacPopulation();      //同mac,同地址在线用户数量

        Player player = playerMapper.selectPlayerByPlayerId(playerId);
        if (location == null || location.equals("")) {
            location = locationMapper.getLocationByPlayerId(playerId + "");
        }

        log.info("收到的" + player.getWxNickname() + "| = w = |" + playerId + "WiFi列表:" + wlanList);
        log.info(player.getWxNickname() + "连到的的WiFi:" + thisMAC);

//      同位置同mac人数上限
        this.checkSameMac(player, thisMAC, location, sameMacPop);

//       4G
        if (Objects.isNull(wlanList)) {
            Collection<Object> values = rs.hGetAll(location).values();
            if (values != null) {
                if (values.size() >= pop_L || values.size() >= pop_S) {
                    log.error("触发4G聚集", new AppException(ResponseEnum.GATHERING_OVER_FLOW));
                    throw new AppException(ResponseEnum.GATHERING_OVER_FLOW);
                }
            }
            wlanList.add(thisMAC);
            this.buildWiFiRelation(wlanList, playerId + "", location);
            return new ResultMessage(ResponseEnum.SUCCESS, null);

        }


//        WiFi
        log.info("当前聚集大上限:" + pop_L);
        log.info("当前聚集小上限:" + pop_S);
        log.info("当前聚集选择项:" + choice);
        log.info("当前同型号上限:" + deviceLimit);
        ResultMessage result = checkGathering(player, wlanList, pop_S, pop_L, choice, deviceLimit, location);          //检测聚集
        if (result.getCode() != "200") {
            return result;
        }

        log.info("聚集验证成功");
        return new ResultMessage(ResponseEnum.SUCCESS, null);
    }


    /***
     * <p>
     * 1.上传设备记录
     * <p>
     * 2.由服务端进行验证
     * <p>
     * 3.聚集验证
     * <p>
     * 4.同时获取激励广告ID
     * @param request   该次http请求
     * @param playerDTO 玩家信息
     * @param json      设备信息+位置信息
     * @return 将要观看的激励广告的id
     */

    @Transactional(rollbackFor = Exception.class)
    public ResultMessage identifyAndGetEncourageID(HttpServletRequest request, PlayerDTO playerDTO, DeviceDTO json) throws UnknownHostException {
        Player p = playerMapper.selectPlayerByPlayerId(playerDTO.getPlayerId());

        String wifi = json.getWifi();
        List<String> wlanList = null;
        if (Objects.nonNull(wifi)) {
            wlanList = JSON.parseArray(wifi, String.class);
        }
        String thisMAC = json.getThisMAC();
        String location = json.getLocation();
        String androidID = json.getAndroidID();
        String oaid = json.getOaid();

        String special = p.getSpecial();
        Character status = p.getStatus();
        Long playerID = playerDTO.getPlayerId();

        String gameId = playerDTO.getGameId().toString();
        //拿到聚集设置
        String UserGatheringSetting_json = forest.getUserGatheringSetting(gameId);
        ResultMessage resultMessage = JSONObject.parseObject(UserGatheringSetting_json, ResultMessage.class);
        UserGatheringSetting gathering = JSON.to(UserGatheringSetting.class, resultMessage.getData());


        DeviceRecordlist deviceRecord = deviceRecordlistMapper.getLastDeviceRecordByPlayerId(playerDTO.getPlayerId());


        if (status == '0') {
            log.error("用户被封禁，禁止登录");
            throw new AppException(ResponseEnum.BAN_USER_OUT);
        }

        DeviceRecordlist deviceRecordlist = deviceRecordlistMapper.getLastDeviceRecordByPlayerId(playerID);

//oaid 安卓id 验证
        if (special.equals("1")) {

            if (deviceRecordlist.getOaid().equals(oaid) == false) {
                log.error("登录异常,oaid与设备记录中的oaid不一致，请检查设备是否更换");
                exRec.uploadException("OAID异常", playerID + "", null,
                        p.getWxNickname(), null, null, androidID, oaid, ResponseEnum.DEVICE_NOT_MATCH + "-- 看激励广告的时候");
                log.info("oaid异常的设备型号已记录");
                return new ResultMessage(ResponseEnum.DEVICE_NOT_MATCH, null);
            }

            if (deviceRecordlist.getAndroidId().equals(androidID) == false) {
                log.error("登录异常,androidId与设备记录中的androidId不一致，请检查设备是否更换");
                exRec.uploadException("安卓ID异常", playerID + "", null,
                        p.getWxNickname(), null, null, androidID, oaid, ResponseEnum.DEVICE_NOT_MATCH + "-- 看激励广告的时候");
                log.info("安卓ID异常的设备型号已记录");
                return new ResultMessage(ResponseEnum.DEVICE_NOT_MATCH, null);
            }

            if (status == '0') {
                log.error("封禁用户，禁止登录");
                exRec.uploadException("登录封禁", playerID + "", null,
                        p.getWxNickname(), null, null, androidID, oaid, ResponseEnum.BAN_USER_OUT + "-- 看激励广告的时候");

                log.info("封禁用户的设备id已刷新");
                return new ResultMessage(ResponseEnum.BAN_USER_OUT, null);
            }
        }


//特殊用户跳过判断 ，只看是否第一次登录
        try {
            if (special.equals("0") == true) {
                SignDto signDto = SignDto.builder().oaid(oaid).androidID(androidID).build();
                if (Objects.isNull(deviceRecordlist)) {
                    log.info("特殊用户首次登录 - 虽然注册了,但设备记录却没有的情况");
                    this.addDeviceRecord(p, signDto, "0");
                }

                if (deviceRecordlist.getOaid().equals(oaid) == false ||
                        deviceRecordlist.getAndroidId().equals(androidID) == false) {
                    log.info("特殊用户设备变更,现已记录");
                    this.addDeviceRecord(p, signDto, "0");
                }
                log.info("特殊用户设备未变更,可以正常登录");
            }
        } catch (Exception e) {
            SpringRollBackUtil.rollBack();
            log.error("特殊用户设备记录添加失败，请联系管理员");
            return new ResultMessage(ResponseEnum.DEVICE_RECORD_ADD_Failed, null);
        }
//位置检测
        this.updateLocation(playerDTO, location);

//wifi聚集 同型号 同mac验证
        ResultMessage resultMSG_1 = this.identification(playerDTO, thisMAC, wlanList, location);
        if (resultMSG_1.getCode() != "200") {
            return resultMSG_1;
        }


//新建 激励广告
        String ipAddr = IpUtils.getIpAddr(request);

        Long playerId = playerDTO.getPlayerId();

        Player player = playerMapper.selectPlayerByPlayerId(playerId);
        String playerIsNewIn = ""; //0是新用户，1是老用户
        if (player.getPlayerCreatTime().isBefore(LocalDateTime.now()) == true) playerIsNewIn = "0";
        else playerIsNewIn = "1";

        AdEncourage encourage = AdEncourage.builder().ip(ipAddr).playerId(playerId)
                .appID(Long.valueOf(gameId))
                .wxNickName(player.getWxNickname()).advTypeId('5').address(location)
                .isOldPlayer(playerIsNewIn).isCloseEncourageAdv("1").isSeeEnd("1")
                .isDisplayAd("1").isServerCall('1').isClientCall('1')
                .build();

        try {
            adEncourageMapper.insertAdEncourage(encourage);
        } catch (Exception e) {
            SpringRollBackUtil.rollBack();
            log.error("添加激励广告记录失败，请联系管理员");
            throw new AppException(ResponseEnum.ADD_AD_ENCOURAGE_FAILED);
        }

        Long encourageId = encourage.getAdvEncourageId();
        String cityInfo = IpUtils.getCityInfo(ipAddr);
        HashMap<String, String> map = new HashMap<>();
        map.put("encourageId", encourageId + "");
        map.put("cityInfo", cityInfo);
        log.info("添加激励广告记录，激励广告ID:" + encourageId);
        return new ResultMessage(ResponseEnum.SUCCESS, map);
    }

    @Resource(name = "GameSettingMapper")
    private GameSettingMapper gameSettingMapper;

    /***
     * 通过包名获取游戏设置
     * @return
     * 1玩家红包余额
     * <p>
     * 2总累计红包金额
     * <p>
     * 3今日累计红包金额
     * <p>
     * 4此时的提现比例
     * <p>
     * 5玩家支付宝id
     * <p>
     * 6玩家姓名
     * <p>
     * @param packageName  应用包名
     * @return
     */
    @Override
    public PlayerMetaData getGameSetting(PlayerDTO playerDTO, String packageName) {
        GameSetting gameSetting = gameSettingMapper.getGameSettingByPackageName(packageName);

        if (Objects.isNull(gameSetting)) {
            log.error("获取游戏设置失败，请检查包名:" + packageName + "是否正确", new AppException(ResponseEnum.GET_GAME_SETTING_FAILED));
            throw new AppException(ResponseEnum.GET_GAME_SETTING_FAILED);
        }

        Double screenVal = gameSettingMapper.getScreenshotSettingByPk(gameSetting.getScreenshotSettingId());//截图设置

        GameSettingVo gameSettingVo = new GameSettingVo();
        BeanUtils.copyProperties(gameSetting, gameSettingVo);   //游戏设置
        PlayerMetaData playerMetaData = new PlayerMetaData();

        Long playerId = playerDTO.getPlayerId();
        Long gameId = playerDTO.getGameId();

        Player player = playerMapper.selectAliPayInfoByPlayerId(playerId);
        DayBehaveRecordlist player_day_record = dayBehaveMapper.getDayLastDayBehaveRecordlistByPlayerId(playerId); //该玩家当日的留存数据

        BigDecimal totalred = player_day_record.getTotalred(); //玩家总累计红包金额
        BigDecimal todayred = player_day_record.getTodayred(); //玩家当日红包余额
        BigDecimal inRed = player.getInRed();                  //玩家余额
        String withdrawPercentage = withdrawMapper.getWithdrawPercentage(gameId);//提现比例
        String payLoginId = player.getPayLoginId(); //支付宝id
        String realName = player.getRealName(); //姓名

//设置
        playerMetaData.setScreenshotSettingVal(screenVal);
        playerMetaData.setGameSettingVo(gameSettingVo);
        playerMetaData.setWithdrawPercentage(withdrawPercentage);
//玩家数据
        playerMetaData.setInRed(String.valueOf(inRed));
        playerMetaData.setTotalRed(String.valueOf(totalred));
        playerMetaData.setTodayRed(String.valueOf(todayred));
//pay
        playerMetaData.setPayLoginId(payLoginId);
        playerMetaData.setRealName(realName);

        log.info("获取游戏设置成功");
        return playerMetaData;
    }

    @Override
    public UserRewardSetting getUserRewardSetting(String packageName) {
        GameSetting gameSetting = gameSettingMapper.getGameSettingByPackageName(packageName);
        Integer userRewardSettingId = gameSetting.getUserRewardSettingId();
        UserRewardSetting userRewardSetting = gameSettingMapper.getUserRewardSetting(gameSetting.getUserRewardSettingId());

        if (Objects.isNull(userRewardSetting)) {
            log.error("获取当前的用户奖励设置失败，请检查用户奖励设置Id:" + userRewardSettingId + "或者包名是否正确", new AppException(ResponseEnum.GET_USER_REWARD_SETTING_FAILED));
            throw new AppException(ResponseEnum.GET_USER_REWARD_SETTING_FAILED);
        }

        log.info("奖励设置获取成功");
        return userRewardSetting;
    }

    /***
     * 存储新用户的WiFi列表和更新该地区的用户列表
     * @param wlanList 新上线玩家此时的wifi列表
     * @param playerId 新上线的id
     * @param location 新上线玩家此时的位置
     */
    public void buildWiFiRelation(List<String> wlanList, String playerId, String location) {
        //存储新玩家的wifi列表和 更新该位置的用户列表
//        if (rs.setMembers(playerId).size() > 0) {
//
//            for (String player_old_mac : rs.setMembers(playerId)) {//删掉该玩家原有的wifi列表
//                if (rs.setMembers(location + player_old_mac).size() - 1 == 0) {
//                    rs.sAdd(location + player_old_mac, "");
//                }
//                rs.sRemove(location + player_old_mac);  //该位置用户id列表
//
//                if (rs.setMembers(player_old_mac).size() - 1 == 0) {
//                    rs.sAdd(player_old_mac, "");
//                }
//                rs.sRemove(player_old_mac); //该位置相关mac列表
//            }
//            rs.delete(playerId);
//        }

        Object o = rs.hGet(location + "AL", playerId);
        if (o != null) {         //同时该玩家原有的位置+序号也需要删掉  (如果该位置有的话)
            String location_index = o.toString();
            rs.hDelete(location, location_index);
            rs.hDelete(location + "AL", playerId);
        }

        for (String wlan : wlanList) {
            rs.sAdd(playerId, wlan);  //新用户的wifi列表
        }

        String thisID = null; //新玩家添加到该位置的在线用户列表 ,没有就直接插  有的话 就做一些非空处理并防止重复插入 在插入
        if (rs.hExists(location, rs.hSize(location) + "") == true) {
            thisID = rs.hGet(location, rs.hSize(location) + "") + "";
            if (thisID.equals(playerId) == false) {
                rs.hPut(location, (rs.hSize(location) + 1) + "", playerId);
                rs.hPut(location + "AL", playerId, rs.hSize(location) + "");
            }
        } else {
            rs.hPut(location, (rs.hSize(location) + 1) + "", playerId);
            rs.hPut(location + "AL", playerId, rs.hSize(location) + "");
        }

    }

    //写个方法recheck() 给list_recheck做聚集判断用 方法返回的是需要踢掉的人的id列表'

    /***
     *
     * @param list_kick         聚集要踢的列表
     * @param list_device_kick  要踢的同型号列表
     * @param playerId          和新上线用户有聚集关系的用户id
     * @param location      新上线玩家此时的位置
     * @param pop_l     大聚集人数上限
     * @param pop_s     小聚集人数上限
     * @param choice    大聚集还是小聚集
     * @param original_A  新登用户
     * @param isSameDevice  是否同台设备
     * @param deviceLimit   同型号上限
     */
    public void reCheck(Set<String> list_kick, Set<String> list_device_kick, String playerId, String location, Integer pop_l, Integer
            pop_s, Character choice, String original_A, boolean isSameDevice, Integer deviceLimit) {

        synchronized (this) {
            List<String> about_a_id_1 = new ArrayList<>();                                           //跟a 一步有关系的用户id列表
            Set<String> list_a = rs.setMembers(playerId);
            Set<String> list_same_device_recheck = new HashSet<>();                                     //同型号设备列表


            Set<String> this_All_mac = new HashSet<>();                       //wifi关系表中的 该位置所有mac
            this_All_mac.addAll(rs.setMembers(location + "MAC"));               //新用户上来之前的MAC关系表

            for (String wlan : list_a) { //遍历a的wifi列表
                log.info(playerId + "recheck中的WiFi列表:" + wlan);
                for (String mac : this_All_mac) {//遍历该定位下的wifi关系表 的 mac字段
                    log.info(playerId + "recheck中遍历一步的MAC:" + mac);
                    if (mac.equals(wlan)) {
                        // 取wifi列表里 该mac对应的wifi相关id列表
                        Set<String> list_id = rs.setMembers(location + mac);
                        if (list_id.size() != 1) { // 记一步有关联的用户
                            for (String id : list_id) {
                                if (!about_a_id_1.contains(id)) {
                                    about_a_id_1.add(id);
                                    log.info(playerId + "recheck中一步用户:" + id + "成功加入");

                                    //用一个Set存其中与其同型号id
                                    if (this.isSameDevice(id, playerId) == true) {
                                        list_same_device_recheck.add(id);
                                    }
                                }
                            }
                        }

                        //取wifi列表里 该mac对应的相关mac列表  见上面的说明
                        Set<String> list_mac = rs.setMembers(mac);
                        for (String mac_1 : list_mac) {
                            log.info(playerId + "recheck中遍历二步的MAC:" + mac);
                            // 取wifi列表里 该mac对应的wifi相关id列表
                            Set<String> list_id_1 = rs.setMembers(location + mac_1);
                            if (list_id_1.size() != 1) { // 记二步有关联的用户
                                for (String id : list_id_1) {

                                    if (!about_a_id_1.contains(id)) {
                                        about_a_id_1.add(id);
                                        log.info(playerId + "recheck中二步用户:" + id + "成功加入");

                                        //用一个Set存其中与其同型号id
                                        if (this.isSameDevice(id, playerId) == true) {
                                            list_same_device_recheck.add(id);
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
            }

            if (about_a_id_1.contains("")) {
                about_a_id_1.remove("");
            }

            if (about_a_id_1.contains("")) {
                about_a_id_1.remove("");
            }


            log.info("ReCheck函数的 ==:>:" + "about_a_id_1人数:" + about_a_id_1 + "about_a_id_1人数:" + about_a_id_1 + "上限:" + pop_l);
            //大聚集 一步+两步的人加起来 比设置的值还大时 就得踢掉包括一步+两步+A的所有人 因为只踢A 剩下的人还是会触发大聚集
            if (about_a_id_1.size() > pop_l) {
                log.info(playerId + "recheck大聚集踢所有人");
                //list_kick加入list_about_a1和list_about_a2里的所有用户和A
                list_kick.addAll(about_a_id_1);
                list_kick.add(original_A);
                log.info("recheck大聚集踢人列表:" + list_kick);
            } else if (about_a_id_1.size() == pop_l) {
                log.info(playerId + "_:recheck大聚集踢A");
                list_kick.add(original_A);
                log.info("recheck大聚集踢人列表:" + list_kick);
            }

            if (list_same_device_recheck.size() > deviceLimit) {
                log.info(playerId + "_:recheck同型号踢所有人");
                list_device_kick.addAll(list_same_device_recheck);
                if (isSameDevice == true) {
                    log.info(playerId + "_:recheck同型号踢所有人也踢A");
                    list_device_kick.add(original_A);
                }
                log.info("recheck同型号踢人列表:" + list_device_kick);
            } else if (isSameDevice == true && (list_same_device_recheck.size() == deviceLimit)) {
                log.info(playerId + "_:recheck同型号踢A");
                list_device_kick.add(original_A);
                log.info("recheck同型号踢人列表:" + list_device_kick);
            }

        }
    }


    /***
     * 这两个playerID的设备ID是否相同
     * @return
     */
    public boolean isSameDevice(String playerID, String compareID) {
        if (playerID == null || compareID == null || playerID.isEmpty() || compareID.isEmpty()) {
            return false;
        }

        Long player_MobileID = playerMapper.selectMobileIDByPlayerId(playerID);
        Long compare_MobileID = playerMapper.selectMobileIDByPlayerId(compareID);
        String player_MobileName = playerMapper.selectMobileNameByPlayerId(playerID);
        String compare_MobileName = playerMapper.selectMobileNameByPlayerId(compareID);
        log.info("玩家:_" + playerID + "的设备id" + player_MobileID);
        log.info("比对玩家:_" + compareID + "的设备id" + compare_MobileID);

        if (player_MobileID == null || compare_MobileID == null) {
            log.error("玩家:" + playerID + "未上传设备信息");
            throw new AppException(ResponseEnum.PLAYER_WITHOUT_UPLOAD_DEVICE_INFO);
        }

        if (player_MobileID == compare_MobileID) {
            log.info("比较后玩家" + playerID + "和比对玩家:_" + compareID + "设备型号相同");
            return true;
        }

        if (player_MobileID == compare_MobileID == false) {
            if (player_MobileName.equals(compare_MobileName) == true) {
                log.info("比较后玩家" + playerID + "和比对玩家:_" + compareID + "两者的设备ID不同但是设备名称相同,所以定为相同的设备型号");
                return true;
            }
        }

        log.info("比较后玩家" + playerID + "和比对玩家:_" + compareID + "设备型号不同");
        return false;
    }

    /***
     * //       检测聚集+同型号(所在地区,WiFi列表)
     * @param player     新上线玩家
     * @param wlanList   新上线玩家此时附近的WiFi列表
     * @param pop_s     小范围上限
     * @param pop_l     大范围上限
     * @param choice    0:小范围 1:大范围
     * @param deviceLimit    同型号设备上限
     * @param location   新上线玩家此时的位置
     * @return 校验结果
     */

    @Transactional(rollbackFor = Exception.class)
    public ResultMessage checkGathering(Player player, List<String> wlanList, Integer pop_s, Integer
            pop_l, Character choice, Integer deviceLimit, String location) {
        Long playerID = player.getPlayerId();
        String playerId = player.getPlayerId() + "";

        Long player_MobileID = playerMapper.selectMobileIDByPlayerId(playerID + "");
        log.info("新上来用户的设备ID:_" + player_MobileID);

//玩家自身上次聚集和这次聚集校验

        this.buildWiFiRelation(wlanList, playerId, location);

        synchronized (this) {
            log.info("当前线程名称:" + Thread.currentThread().getName());
            log.info("当前线程ID:" + Thread.currentThread().getId());
            Set<String> list_a = rs.setMembers(playerId);
            Set<String> list_a2 = new HashSet<>();                                     //a的wifi列表中 但在wifi关系表中没有的mac列表
            list_a2.addAll(list_a);

            boolean trigger = false;//是否有进入此并集逻辑中

            Set<String> list_kick = new HashSet<>();                                           //需要踢的所有用户的id列表（开始是空的）
            Set<String> list_device_kick = new HashSet<>();                                     //同型号需要踢的所有用户的id列表（开始是空的）
            List<String> about_a_id_1 = new ArrayList<>();                                           //跟a 一步有关系的用户id列表
            List<String> about_a_id_2 = new ArrayList<>();                                          //跟a 二步有关系的用户id列表
            Set<String> list_same_device = new HashSet<>();                                          //同型号列表
            Set<String> list_recheck = new HashSet<>();                                          //需要重新查聚集的用户的id列表

            Set<String> this_All_mac = new HashSet<>();                       //wifi关系表中的 该位置所有mac
            this_All_mac.addAll(rs.setMembers(location + "MAC"));
            log.info("新用户上来之前的MAC关系表:" + this_All_mac);

            for (String wlan : wlanList) { //遍历a的wifi列表
                for (String mac : this_All_mac) {//遍历该定位下的wifi关系表 的 mac字段
                    log.info("遍历WiFi关系表的MAC:" + mac);
                    if (mac.equals(wlan)) {
                        // 取wifi列表里 该mac对应的wifi相关id列表
                        Set<String> list_id = rs.setMembers(location + mac);
                        if (list_id.size() != 1) { // 记一步有关联的用户
                            for (String id : list_id) {
                                if (!about_a_id_1.contains(id)) {
                                    about_a_id_1.add(id);
                                    if (this.isSameDevice(id, playerId) == true) {
                                        list_same_device.add(id);
                                    }
                                }
                            }
                        }

                        //取wifi列表里 该mac对应的相关mac列表  见上面的说明
                        Set<String> list_mac = rs.setMembers(mac);
                        for (String mac_1 : list_mac) {
                            // 取wifi列表里 该mac对应的wifi相关id列表
                            Set<String> list_id_1 = rs.setMembers(location + mac_1);
                            if (list_id_1.size() != 1) { // 记二步有关联的用户
                                for (String id : list_id_1) {
                                    if (!about_a_id_1.contains(id) && !about_a_id_2.contains(id)) {
                                        about_a_id_2.add(id);
                                        if (this.isSameDevice(id, playerId) == true) {
                                            list_same_device.add(id);
                                        }
                                    }
                                }
                            }
                        }

                        for (String s : list_a) {

                            //if(存在一个或多个mac，它在list_a里有，而在maci对应的 '与该mac关联的mac（列表）'里没有)  //意味着wifi关系表需要更新
                            if (rs.setMembers(mac).contains(s) == false) {
                                log.info("找到是这个:+++>" + s);
                                trigger = true;

                                //更新wifi关系表
                                // 把 mac 对应的 '与该mac关联的mac（列表）'字段更新为'与该mac关联的mac（列表）'与list_a的并集，并集里不包括ai
                                Set<String> s_union = rs.sUnion(mac, playerId);
                                s_union.remove(mac);
                                rs.delete(mac);
                                for (String mac_1 : s_union) {
                                    rs.sAdd(mac, mac_1);
                                }
                                break;
                            }
                        }

                        list_a2.remove(wlan);
                        break;
                    }
                }
            }

            if (about_a_id_2.contains("")) {
                about_a_id_2.remove("");
            }

            if (about_a_id_1.contains("")) {
                about_a_id_1.remove("");
            }

            if (about_a_id_2.contains(playerId)) {
                about_a_id_2.remove(playerId);
            }

            if (about_a_id_1.contains(playerId)) {
                about_a_id_1.remove(playerId);
            }

            log.info("list_a2: " + list_a2);

//继续更新wifi关系表
            if (list_a2.size() > 0) {
                for (String ai : list_a2) //遍历A的wifi列表中没在wifi关系表里的mac
                {
                    //wifi关系表中新增一条
                    rs.sAdd(location + ai, ""); //mac关系表 的用户id + mac
                    //                rs.sRemove(location + ai, "");

                    //mac关系表 的相关联mac   为不包括ai的list_a
                    Set<String> set = rs.setMembers(playerId);
                    set.remove(ai);
                    for (String id : set) {
                        rs.sAdd(ai, id);
                    }
                    this_All_mac.add(ai);
                    rs.sAdd(location + "MAC", ai);
                }

            }

            for (Object value : rs.hGetAll(location).values()) {
                String s = value.toString();
                for (String sm : rs.setMembers(s)) {
                    Set<String> strings = rs.setMembers(sm);
                    log.info("相关mac关系表打印:" + sm + ":  " + strings);
                    log.info("用户id列表打印:" + sm + ":  " + rs.setMembers(location + sm));
                }
            }


//处理聚集
// 先看大聚集
//一步+两步的人加起来 比设置的值还大时 就得踢掉包括一步+两步+A的所有人 因为只踢A 剩下的人还是会触发大聚集
            log.info("about_a_id_2人数:" + about_a_id_2 + "about_a_id_1人数:" + about_a_id_1 + "上限:" + pop_l);
            if (about_a_id_2.size() + about_a_id_1.size() >= pop_l) {
                log.info("大聚集触发");
                list_kick.add(playerId);
            }
            //大聚集没触发
            else if (choice == '0') {  //聚集设置选择小范围
                if (about_a_id_1.size() >= pop_s) {
                    log.info("小聚集触发");
                    list_kick.add(playerId);
                }
            }

            if (list_same_device.size() >= deviceLimit) {
                log.info("同型号触发");
                list_device_kick.add(playerId);
            }


//查看其它人是否有聚集
            if (trigger == true) {
                for (String s : about_a_id_1) {
                    this.reCheck(list_kick, list_device_kick, s, location, pop_l, pop_s, choice, playerId, this.isSameDevice(s, playerId), deviceLimit);
                }
                for (String s : about_a_id_2) {
                    this.reCheck(list_kick, list_device_kick, s, location, pop_l, pop_s, choice, playerId, this.isSameDevice(s, playerId), deviceLimit);
                }
                trigger = false;
            }

            log.info("聚集要踢的人:" + list_kick);
            log.info("同型号要踢的人:" + list_device_kick);
            //踢人
            for (String k : list_device_kick) {
                sessionUtils.userExit(k, location);
            }

            if (list_kick.contains(playerId)) {
                for (String k : list_kick) {
                    sessionUtils.userExit(k, location);
                }
                throw new AppException(ResponseEnum.GATHERING_OVER_FLOW);
            } else {
                if (list_device_kick.contains(playerId)) {
                    log.error("触发同型号", new AppException(ResponseEnum.DEVICE_OVER_FLOW));
                    throw new AppException(ResponseEnum.DEVICE_OVER_FLOW);
                } else {
                    for (String mac : this_All_mac) { //遍历该定位下的wifi关系表
                        for (String s1 : list_a) {
                            if (mac.equals(s1)) {
                                rs.sAdd(location + mac, playerId);//mac对应的 wifi相关id列表里 加上A的id
                            }
                        }
                    }
                }
            }

            return new ResultMessage(ResponseEnum.SUCCESS, null);
        }
    }


    /***
     *
     * 检测同mac下同位置的人数
     * @param player 新上线玩家
     * @param thisMac 新上线玩家的wifi
     * @param location 新上线玩家的位置
     * @param pop 同mac上限
     */
    public void checkSameMac(Player player, String thisMac, String location, Integer pop) {
        Collection<Object> values = rs.hGetAll(location + "Mac").values();
        long count = values.stream().filter(o -> o.equals(thisMac)).count();

        if (count > pop) {
            log.error(location + "同mac地址超出人数上限|>" + player.getPlayerId() + "<|玩家已记录");
            rs.hDelete(location + "Mac", player.getPlayerId() + "");  //删除该上线玩家的mac
            throw new AppException(ResponseEnum.SAME_MAC_OVER_FLOW);
        }

    }

}
