package com.yunting.login.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yunting.common.Dto.PlayerDTO;
import com.yunting.common.Dto.RiskControlSetting;
import com.yunting.common.exception.AppException;
import com.yunting.common.results.ResponseEnum;
import com.yunting.common.results.ResultMessage;
import com.yunting.common.utils.*;
import com.yunting.login.dto.*;
import com.yunting.login.entity.*;
import com.yunting.login.mapper.*;
import com.yunting.login.utils.ExceptionRecording;
import com.yunting.login.utils.WxRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service("LoginService")
@Slf4j
public class LoginService {
    @Resource(name = "PlayerMapper")
    private PlayerMapper playerMapper;

    @Resource(name = "LocationMapper")//定位表
    private LocationMapper locationMapper;

    @Resource(name = "MobileDetailMapper")
    private MobileDetailMapper mobileDetailMapper;

    @Resource(name = "DayBehaveRecordlistMapper")
    private DayBehaveRecordlistMapper dayBehaveMapper;

    @Resource(name = "DeviceRecordlistMapper")
    private DeviceRecordlistMapper deviceRecordlistMapper;

    @Resource(name = "ExceptionRecording")
    private ExceptionRecording exRec;

    @Resource(name = "RedisUtils")
    private RedisUtils_Wlan rs;

    @Resource(name = "RedisUtil_session")
    private RedisUtil_session rus;

    @Resource(name = "JWTutil")
    JWTutil jwTutil;

    @Resource(name = "WxRequestUtil")
    private WxRequestUtil wxUtil;

    @Resource(name = "ST")
    private ST st;


    /***
     * 手动添加添加设备信息接口
     * @param mobileDetail 设备信息
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


    /***
     * 采集并上传设备信息
     * @param mobileDetail 设备信息
     * @return 返回允许的设备品牌
     *
     */
    @Transactional(rollbackFor = Exception.class)
    public CollectionVo collectionAndUploadMobileInfo(MobileDetail mobileDetail) {
        String deviceName = mobileDetail.getDeviceType();
        this.saveAndDistinct(mobileDetail);
        DeviceBrand approvalBrand = mobileDetailMapper.getAllImapprovalBrand(deviceName);
        log.info("Int_设备信息采集成功,禁用设备表已返回");
        CollectionVo vo = CollectionVo.builder()
                .appstore(approvalBrand.getAppstore())
                .installMachine(approvalBrand.getInstallMachine())
                .time(System.currentTimeMillis())
                .zeroTime(TimeUtils.getTimeStamp())
                .build();
        return vo;
    }


    /***
     * 获取用户提现记录
     * @param playerDTO 玩家信息
     * @return 用户提现记录
     */
    public List getPlayerWithdrawRecord(PlayerDTO playerDTO, Integer pageNum) {
        Long playerId = playerDTO.getPlayerId();

        List<WithdrawVo> record = locationMapper.getWithdrawRecordByPlayerIdAndPackageName(playerId, st.PackageName(), pageNum);
        log.info("用户提现记录已返回,第" + pageNum + "页的");
        return record;
    }


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
     * 7玩家任务列表
     */
    public PlayerMetaData getGameSetting(PlayerDTO playerDTO) {
        PlayerMetaData playerMetaData = new PlayerMetaData();

        Long playerId = playerDTO.getPlayerId();

        ScreenshotTask task = playerMapper.getFirstTaskListByPlayerIdAndGameID(playerId, st.GameId());
        Player player = playerMapper.selectAliPayInfoByPlayerId(playerId, st.GameId());
        DayBehaveRecordlist player_day_record = dayBehaveMapper.getDayLastDayBehaveRecordlistByPlayerId(playerId); //该玩家当日的留存数据

        if (player_day_record == null) {
            player_day_record.setTotalred(BigDecimal.ZERO);
            player_day_record.setTodayred(BigDecimal.ZERO);
            player_day_record.setTodayEncourageAdvCount(0);
        }
        BigDecimal totalred = player_day_record.getTotalred(); //玩家总累计红包金额
        BigDecimal todayred = player_day_record.getTodayred(); //玩家当日红包余额
        BigDecimal inRed = player.getInRed();                  //玩家余额
        String payLoginId = player.getPayLoginId(); //支付宝id
        String realName = player.getRealName(); //姓名
//设置
        //截图设置
        playerMetaData.setScreenshotSettingVal(st.Codebit_Max_val());
        playerMetaData.setTransLimitDaily(st.Daily_Max_Submit_Num());
        playerMetaData.setTransRewardCont(st.Daily_Max_Watch_Num());
        String shotOptions = st.isShot_Switch() ? "1" : "0";
        playerMetaData.setScreenshotSettingOptions(shotOptions);

        playerMetaData.setNoticeMSG(st.Notification());
        playerMetaData.setAdvWatchInterval(st.ADV_Interval());
        playerMetaData.setWithdrawPercentage(st.Withdraw_Percentage());//提现比例
//玩家数据
        playerMetaData.setInRed(String.valueOf(inRed));
        playerMetaData.setTotalRed(String.valueOf(totalred));
        playerMetaData.setTodayRed(String.valueOf(todayred));
//pay
        playerMetaData.setPayLoginId(payLoginId);
        playerMetaData.setRealName(realName);

//任务
        if (task == null || task.getBonus() == null) {
            playerMetaData.setBonus(BigDecimal.ZERO);
            playerMetaData.setTaskProcess(0);
            log.info("玩家:|-" + playerId + "-|,任务金不存在,现已读取游戏设置");
            return playerMetaData;
        }
        playerMetaData.setBonus(task.getBonus());
        playerMetaData.setTaskProcess(task.getTaskProcess());
        log.info("玩家:|-" + playerId + "-|已读取游戏设置");
        return playerMetaData;
    }


    /***
     * 没有token
     * <p>
     * 1微信登录 | 注册  ☆
     * <p>
     * 2上传设备记录和地址
     * <p>
     * 3由服务端进行验证
     * <p>
     * 4传风控参数
     *
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultMessage PlayerSignOn(HttpServletRequest request, SignDto signDto) {
        String wxCode = signDto.getWxCode();          //微信code
        String proxyName = signDto.getProxyName();    //标识名
        String location = signDto.getLocation();    //地址

        String androidID = signDto.getAndroidID();      //安卓id
        String oaid = signDto.getOaid();                //oaid
        String deviceType = signDto.getDeviceType();    //设备品牌
        String deviceDetail = signDto.getDeviceDetail(); //设备型号

        String mobileCpu = signDto.getMobileCpu();          //CPU
        String mobileSystem = signDto.getMobileSystem();    //设备系统
        String mobileCpuFluency = signDto.getMobileCpuFluency(); //CPU频率

        RiskControlSetting riskControlSetting = st.Risk();//风控参数
        log.info("参数以获取" + riskControlSetting);
        String cityInfo = IpUtils.getCityInfo(IpUtils.getIpAddr(request));  //地址

//        先判断注册还是登录
        if (StringUtils.isEmpty(st.PackageName()) || StringUtils.isEmpty(proxyName) || StringUtils.isEmpty(wxCode)) {
            log.error("包名||微信code||标识名称为空");
            throw new AppException(ResponseEnum.PARAMETER_NOT_FOUND);
        }

        String openid = wxUtil.getWxOpenID(st.PackageName(), wxCode); //获取微信openID
        LocalTime beginTime = LocalTime.parse(st.Forbid_Begin_Time()); //获取禁止看广告的时间
        LocalTime endTime = LocalTime.parse(st.Forbid_End_Time());

        MobileDetail md = MobileDetail.builder().deviceType(deviceType).deviceDetail(deviceDetail)
                .mobileCpu(mobileCpu).mobileCpuFluency(mobileCpuFluency).mobileSystem(mobileSystem).build();
        Long mobileID = this.saveAndDistinct(md);//存储+去重(设备型号,更新安卓系统....)+返回该次的设备id

        Player player_isRegistered = playerMapper.selectPlayerByWxOpenId(openid);

        if (Objects.nonNull(player_isRegistered) == true) { //这里是老用户
            Long player_isRegistered_ID = player_isRegistered.getPlayerId();

            //通过玩家id 查找当前要登录的玩家是否是已在线的玩家  若是,则报错
            if (Objects.nonNull(rus.get(player_isRegistered + "State"))) {
                log.error("当前要登录的用户是已在线的用户");
                return new ResultMessage(ResponseEnum.USER_IS_ONLINE, null);
            }


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

            GameMeta gameMeta = new GameMeta();
            BeanUtils.copyProperties(playerDTO, gameMeta);
            gameMeta.setRiskControlSetting(riskControlSetting);
            gameMeta.setAddress(cityInfo);

            gameMeta.setDayOfWeek(TimeUtils.getDayOfWeek());

            gameMeta.setBeginTimeInterval(TimeUtils.getThisTimeStamp(beginTime.getHour(), beginTime.getMinute()));
            gameMeta.setEndTimeInterval(TimeUtils.getThisTimeStamp(endTime.getHour(), endTime.getMinute()));
            gameMeta.setForbidSwitch(st.isForbid_Switch());
            gameMeta.setEnableWeekend(st.IS_Weekend());

            examIsWatchAdv(gameMeta); //获取此时登录的时间段能否可以看广告

            playerMapper.updatePlayerMobileID(player_isRegistered_ID, mobileID);
            log.info("玩家设备id已刷新");
            String isRetain = rur.get("No_Retain" + player_isRegistered_ID);//是否有留存
            if (isRetain == null) {
                rur.setEx("No_Retain" + player_isRegistered_ID, location, TimeUtils.ONE_DAY_MILLISECONDS(), TimeUnit.SECONDS);
            }
            log.info(player_isRegistered.getWxNickname() + "用户已经存在，开始登录");
            return new ResultMessage(ResponseEnum.SUCCESS, gameMeta);
        } else {

//        注册的话就保存设备记录,新建玩家,然后返回风控参数
            Player wxUser = wxUtil.getWxUser(proxyName, mobileID);/*请求微信拿到用户信息,在这一步表中已经生成新玩家了*/
            try {
                playerMapper.insertPlayer(wxUser);//保存新玩家
                PlayerDTO playerDTO = this.generateToken(wxUser);//生成token
                this.generateNewUserDayRecord(playerDTO);//生成新玩家留存记录
                this.addDeviceRecord(wxUser, signDto, "1");//保存新设备记录
                //保存位置信息
                Location location_new = Location.builder()
                        .playerId(playerDTO.getPlayerId())
                        .location(location)
                        .recordTime(LocalDateTime.now())
                        .build();
                locationMapper.insertLocation(location_new);

                //返回前端数据
                GameMeta gameMeta = new GameMeta();
                BeanUtils.copyProperties(playerDTO, gameMeta);
                gameMeta.setRiskControlSetting(riskControlSetting);
                gameMeta.setAddress(cityInfo);

                gameMeta.setDayOfWeek(TimeUtils.getDayOfWeek());
                gameMeta.setBeginTimeInterval(TimeUtils.getThisTimeStamp(beginTime.getHour(), beginTime.getMinute()));
                gameMeta.setEndTimeInterval(TimeUtils.getThisTimeStamp(endTime.getHour(), endTime.getMinute()));
                gameMeta.setForbidSwitch(st.isForbid_Switch());
                gameMeta.setEnableWeekend(st.IS_Weekend());

                log.info(wxUser.getWxNickname() + "玩家,注册成功");
                rur.setEx("No_Retain" + playerDTO.getPlayerId(), location, TimeUtils.ONE_DAY_MILLISECONDS(), TimeUnit.SECONDS);
                return new ResultMessage(ResponseEnum.SUCCESS, gameMeta);
            } catch (Exception e) {
                SpringRollBackUtil.rollBack();
                log.error("注册失败,请检查操作");
                throw new AppException(ResponseEnum.REGISTER_FAILED);
            }
        }
    }

    @Resource(name = "RedisUtil_Record")
    private RedisUtil_Record rur;

    public void generateNewUserDayRecord(PlayerDTO playerDTO) {
        DayBehaveRecordlist dayRecord = DayBehaveRecordlist.builder().appId(st.APPID())
                .playerId(playerDTO.getPlayerId() + "")
                .isRetain("0")
                .build();
        dayBehaveMapper.insertDayBehaveRecordlist(dayRecord);
        log.info("新用户" + playerDTO.getPlayerId() + "当日记录已留存");
    }

    /***
     *
     * 判断当前时间段是否可以看广告
     * @param gameMeta
     */
    private void examIsWatchAdv(GameMeta gameMeta) {
//        if (LocalDate.now().getDayOfWeek().getValue() == 6 || LocalDate.now().getDayOfWeek().getValue() == 7) {
//            if (st.IS_Weekend() == false) {
//                log.info("今天是周六,周日,不可以看广告");
//                gameMeta.setWeekend(false);
//            } else {
//                gameMeta.setWeekend(true);
//            }
//        } else {
//            gameMeta.setWeekend(true);
//        }
//
//        if (LocalDateTime.now().isAfter(LocalDateTime.now().withHour(st.Forbid_Begin_Time()))
//                && LocalDateTime.now().isBefore(LocalDateTime.now().withHour(st.Forbid_End_Time()))) {
//            if (st.isForbid_Switch()) {
//                log.info("您已到达不准看广告的时间段");
//                gameMeta.setForbid(true);
//            } else {
//                gameMeta.setForbid(false);
//            }
//        } else {
//            gameMeta.setForbid(false);
//        }
    }

    /***
     * 添加设备记录
     * <p>
     * (只单纯添加设备记录,没包含去重等任何逻辑)
     * @param dto 玩家的设备信息
     * @param identifiction 玩家的位置
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
     * 存储+去重(设备型号,安卓系统....)+返回该次的设备id
     * @param mobileDetail 设备信息
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Long saveAndDistinct(MobileDetail mobileDetail) {
        String mobileSystem = mobileDetail.getMobileSystem(); //传过来的设备系统
        log.info("传过来的设备系统:" + mobileDetail);

        MobileDetail mobile = mobileDetailMapper.selectMobileNameAndBrand(mobileDetail.getMobileCpu(), mobileDetail.getMobileCpuFluency(), mobileDetail.getDeviceDetail(), mobileDetail.getDeviceType());

        if (Objects.nonNull(mobile)) {  //查到了
            Long mobileID = mobile.getMobileId();
            String localSystem = mobile.getMobileSystem();
            log.info("本地设备系统:" + localSystem);
//            if (localSystem.equals(mobileSystem) == false) {  //其他相同但是设备系统需要更新
//                mobileDetailMapper.changeMobileSystem(mobileID, mobileDetail.getMobileSystem());
//                log.info("该型号设备系统已变更");
//            }
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
                log.info("未查到相关型号数据,新增一条型号数据,设备信息已上传");
                return detail.getMobileId();
            } catch (Exception e) {
                SpringRollBackUtil.rollBack();
                log.error("新增设备型号数据常常出错......");
                throw new AppException(ResponseEnum.MOBILE_INFO_ADD_FAILED);
            }
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
}
