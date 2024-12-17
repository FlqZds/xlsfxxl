package com.yunting.adminservice;


import com.alibaba.fastjson2.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.yunting.client.DTO.PlayerInfoDto;
import com.yunting.client.DTO.RetainActive;
import com.yunting.client.DTO.VO.RetainActiveVO;
import com.yunting.client.DTO.incondition;
import com.yunting.client.common.utils.RedisUtil_session;
import com.yunting.client.common.utils.sessionUtils;
import com.yunting.client.entity.*;
import com.yunting.client.entity.setting.GameSetting;
import com.yunting.client.mapper.Adv.*;
import com.yunting.client.mapper.Client.DeviceRecordlistMapper;
import com.yunting.client.mapper.Client.LocationMapper;
import com.yunting.client.mapper.Client.PlayerMapper;
import com.yunting.client.mapper.Client.WithdrawRecordMapper;
import com.yunting.client.mapper.DayBehaveRecordlistMapper;
import com.yunting.client.mapper.ExceptionRecordlsitMapper;
import com.yunting.client.mapper.MobileDetailMapper;
import com.yunting.client.mapper.OperationRecordlistMapper;
import com.yunting.common.exception.AppException;
import com.yunting.common.results.ResponseEnum;
import com.yunting.common.results.ResultMessage;
import com.yunting.common.utils.SpringRollBackUtil;
import com.yunting.forest.ForestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.websocket.Session;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.yunting.client.Ws.Wso.SESSION_POOL;


@Slf4j
@Service("AdminService")
public class Adminimpl implements AdminService {

    @Resource(name = "PlayerMapper")
    private PlayerMapper playerMapper;

    @Resource(name = "LocationMapper")
    private LocationMapper locationMapper;

    @Resource(name = "DeviceRecordlistMapper")
    private DeviceRecordlistMapper deviceRecordlistMapper;

    @Resource(name = "AdRowstyleMapper")
    private AdRowstyleMapper adRowstyleMapper;
    @Resource(name = "AdInscreenMapper")
    private AdInscreenMapper adInscreenMapper;
    @Resource(name = "AdOpenscreenMapper")
    private AdOpenscreenMapper adOpenscreenMapper;
    @Resource(name = "AdStreamMapper")
    private AdStreamMapper adStreamMapper;
    @Resource(name = "AdEncourageMapper")
    private AdEncourageMapper adEncourageMapper;

    @Resource(name = "DayBehaveRecordlistMapper")
    private DayBehaveRecordlistMapper dayBehaveMapper;

    @Resource(name = "WithdrawRecordMapper")
    private WithdrawRecordMapper withdrawRecordMapper;

    @Resource(name = "ExceptionRecordlsitMapper")
    private ExceptionRecordlsitMapper exceptionRecordMapper;

    @Resource(name = "OperationRecordlistMapper")
    private OperationRecordlistMapper operationRecordMapper;

    @Resource(name = "MobileDetailMapper")
    private MobileDetailMapper mobileDetailMapper;

    @Override
    public PageInfo queryAllMobileInfo(RetainActive active) {
        log.info("设备信息查询条件" + active);
        //        查到该游戏的所有玩家信息
        PageHelper.startPage(active.getPage(), 10);
        List<MobileDetail> details = mobileDetailMapper.selectAllMobileByCondition(active);
        PageInfo pageInfo = new PageInfo(details);

        log.info("设备信息已返回：" + pageInfo);

        return pageInfo;
    }

    @Override
    public PageInfo queryAllOperation(RetainActive active) {
        log.info("异常信息查询条件" + active);
        //        查到该游戏的所有玩家信息
        PageHelper.startPage(active.getPage(), 10);
        List<OperationRecordlist> operationRecord = operationRecordMapper.selectAllRecordByCondition(active);
        PageInfo pageInfo = new PageInfo(operationRecord);

        log.info("操作记录已返回：" + pageInfo);
        return pageInfo;
    }

    @Override
    public PageInfo queryAllException(RetainActive active) {
        log.info("异常信息查询条件" + active);
        //        查到该游戏的所有玩家信息
        PageHelper.startPage(active.getPage(), 10);
        List<ExceptionRecordlsit> allException = exceptionRecordMapper.getAllExceptionRecord(active);
        PageInfo pageInfo = new PageInfo(allException);

        log.info("异常信息数据" + pageInfo);
        return pageInfo;
    }


    @Override
    public PageInfo queryPlayerAnyDetailByCondition(String appid, Long playerId, String condition, Integer page, String recordTime) {


        LocalDate localDate = null;
        if (Objects.nonNull(recordTime) && recordTime.equals("0") == false) {
            localDate = LocalDateTime.parse(recordTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toLocalDate();
        }

        Player player = playerMapper.selectPlayerByPlayerId(playerId);


        switch (condition) {
            case "1": {  //红包
                PageHelper.startPage(page, 10);
                List<DayBehaveRecordlist> playerDayBehave = dayBehaveMapper.getDayBehaveRecordlistByPlayerIdAndTime(playerId, localDate);  //这个玩家行为记录

                PageInfo pageInfo = new PageInfo(playerDayBehave);
                log.info(player.getWxNickname() + "的每日红包数量已返回");
                return pageInfo;
            }
            case "2": {  //位置信息

                PageHelper.startPage(page, 10);
                List<Location> locations = locationMapper.getLocationInfoByPlayerID(playerId, localDate);
                PageInfo pageInfo = new PageInfo(locations);

                log.info(player.getWxNickname() + "的位置信息已返回");
                return pageInfo;
            }
            case "3": {  //激励广告数量
                PageHelper.startPage(page, 10);
                List<DayBehaveRecordlist> playerDayBehave = dayBehaveMapper.getDayBehaveRecordlistByPlayerIdAndTime(playerId, localDate);  //这个玩家行为记录

                PageInfo pageInfo = new PageInfo(playerDayBehave);

//                每日观看激励广告数量已返回
                log.info(player.getWxNickname() + "的每日观看激励广告数量已返回");
                return pageInfo;
            }

        }

        return null;
    }

    @Resource(name = "ForestService")
    private ForestService forestService;

    @Override
    public PageInfo queryRetainActive(RetainActive retainActive) {
        log.info("留存查询条件" + retainActive);
        String resultMessage = forestService.getGameSetting(retainActive.getAppid().toString());
        ResultMessage to = JSON.to(ResultMessage.class, resultMessage);
        GameSetting gameSetting = JSON.to(GameSetting.class, to.getData());
        Integer activeStandard = Integer.parseInt(gameSetting.getActiveStandard());  //每日活跃标准
        log.info("活跃标准:" + activeStandard);

        ArrayList<RetainActiveVO> activeVOS = new ArrayList<>();
        //        查到该游戏的所有玩家信息
        PageHelper.startPage(retainActive.getPage(), 10);
        List<RetainActiveVO> dayRecord = dayBehaveMapper.getRetainActiveByPlayerId(retainActive);
        PageInfo pageInfo = new PageInfo(dayRecord);

        for (RetainActiveVO retainActiveVO : dayRecord) {
            retainActiveVO.setIsRetain("是");
            if (retainActiveVO.getTodayEncourageAdvCount() > activeStandard) {
                retainActiveVO.setIsActive("是");
            } else {
                retainActiveVO.setIsActive("否");
            }
            activeVOS.add(retainActiveVO);
        }
        pageInfo.setList(activeVOS);


        log.info("留存数据" + pageInfo);

        return pageInfo;
    }

    //玩家详细的信息
//    因为是后台想要查不同的后台应用，所以肯定是发送指定后台的appid（当然后续可以自己指定包名）
    @Override
    public PageInfo sendPlayerInfo(RetainActive active) {
        List playerInfos = new ArrayList<PlayerInfoDto>();

//        查到该游戏的所有玩家信息
        PageHelper.startPage(active.getPage(), 10);
        List<Player> players = playerMapper.selectPlayersInQueryCondition(active);
        PageInfo pageInfo = new PageInfo(players);

        for (Player player : players) {
            DeviceRecordlist player_deviceRecord = deviceRecordlistMapper.getLastDeviceRecordByPlayerId(player.getPlayerId());

            MobileDetail mobileDetail = null;
            if (player.getMobileId() != null) {
                mobileDetail = mobileDetailMapper.selectByPrimaryKey(player.getMobileId());
            }

//            if(Objects.isNull(mobileDetail)){
//                mobileDetail.setMobileCpu("空");
//                mobileDetail.setMobileCpuFluency("空");
//                mobileDetail.setMobileSystem("空");
//                mobileDetail.setDeviceName("空");
//            }

            List<DayBehaveRecordlist> daybv = dayBehaveMapper.getDayBehaveRecordlistByPlayerId(player.getPlayerId());

            if (daybv == null || player_deviceRecord == null) {
                log.warn("玩家:" + player.getPlayerId() + "数据异常");
                log.error("未找到ID为:" + player.getPlayerId() + "的玩家设备记录");
                continue;
            }

            BigDecimal totalRed = BigDecimal.ZERO;
            for (DayBehaveRecordlist d : daybv) {
                totalRed = totalRed.add(d.getTotalred());
            }


            String playerIsNewIn = "";
            if (player.getPlayerCreatTime().toLocalDate().isBefore(LocalDate.now()) == true) playerIsNewIn = "老用户";
            else playerIsNewIn = "新用户";


            PlayerInfoDto playerInfoDto = new PlayerInfoDto();

//            信息
            playerInfoDto.setGameid(player.getGameId());
            playerInfoDto.setPlayerId(player.getPlayerId());
            playerInfoDto.setWxOpenId(player.getWxOpenId());
            playerInfoDto.setPayLoginId(player.getPayLoginId());
            playerInfoDto.setWxNickname(player.getWxNickname());
            playerInfoDto.setRealName(player.getRealName());
            playerInfoDto.setProxyId(player.getProxyId());

//            钱
            playerInfoDto.setRedHad(player.getInRed());
            playerInfoDto.setTotalRed(totalRed);
            playerInfoDto.setIsNewIn(playerIsNewIn);

//            设备
            playerInfoDto.setAndroidID(player_deviceRecord.getAndroidId());
            playerInfoDto.setOaid(player_deviceRecord.getOaid());
            playerInfoDto.setDeviceType(player_deviceRecord.getDeviceType());
            playerInfoDto.setDeviceDetail(player_deviceRecord.getDeviceDetail());

            if (mobileDetail != null) {
                playerInfoDto.setMobileCpu(mobileDetail.getMobileCpu());
                playerInfoDto.setMobileCpuFluency(mobileDetail.getMobileCpuFluency());
                playerInfoDto.setMobileSystem(mobileDetail.getMobileSystem());
                playerInfoDto.setDeviceName(mobileDetail.getDeviceName());
            }

//            状态
            playerInfoDto.setBanStatus(player.getStatus());
            playerInfoDto.setSpecial(player.getSpecial());
            playerInfoDto.setPlayerCreatTime(player.getPlayerCreatTime());

            playerInfos.add(playerInfoDto);
            log.info("玩家信息:" + playerInfoDto);
        }

        pageInfo.setList(playerInfos);

        return pageInfo;
    }


    @Resource(name = "OperationRecordlistMapper")
    private OperationRecordlistMapper operationMapper;

    @Resource(name = "RedisUtil_session")
    private RedisUtil_session rus;

    @Resource(name = "GatheringUtils")
    private sessionUtils sessionUtils;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultMessage changeUserPower(incondition incondition) throws IOException {

        String condition_json = incondition.getCondition();

        incondition condition = JSON.to(incondition.class, condition_json);

        Long playerId = Long.parseLong(condition.getPlayerId());
        Player player = playerMapper.selectPlayerByPlayerId(playerId);

        Character status = player.getStatus();
        String power = player.getSpecial();

        OperationRecordlist operationRecord = OperationRecordlist.builder()
                .operationReason(condition.getOperationMsg()).operationTime(LocalDateTime.now())
                .wxOpenId(player.getWxOpenId()).playerId(player.getPlayerId()).wxNickname(player.getWxNickname())
                .build();
        try {
            switch (condition.getCondition()) {
                case "ban": {  //封号
                    if (status == 0) {
                        log.error("用户已封禁，无需再次封禁");
                        return new ResultMessage(ResponseEnum.ALEREADY_BANNED, null);
                    }
                    playerMapper.changePlayerStatus(condition.getPlayerId(), '0', condition.getAppid());
                    operationRecord.setOperationType("封禁");
                    operationMapper.insert(operationRecord);

                    if (rus.hExists(playerId + "", "pos") == true) { //玩家在线,就能拿到值
                        String location = rus.hGet(playerId + "", "pos").toString();

                        Session session = SESSION_POOL.get(playerId + "");
                        Gson gson = new Gson();
                        session.getAsyncRemote().sendText(gson.toJson(ResponseEnum.BAN_USER_OUT));
                        session.close();
                        SESSION_POOL.remove(playerId + "");
                        sessionUtils.userExit(playerId + "", location); //移除玩家的redis在线状态
                    }
                    log.info("用户操作已完成,封号");
                    break;
                }
                case "unban": {  //解封
                    if (status == 1) {
                        log.error("用户已解封，无需再次解封");
                        throw new AppException(ResponseEnum.ALEREADY_UNBANED);
                    }
                    playerMapper.changePlayerStatus(condition.getPlayerId(), '1', condition.getAppid());
                    operationRecord.setOperationType("解封");
                    operationMapper.insert(operationRecord);
                    log.info("用户操作已完成,解封");
                    break;
                }
                case "spec": {  //赋权
                    if (power.equals("0")) {
                        log.error("用户已是特殊用户，无需再次赋权");
                        throw new AppException(ResponseEnum.ALEREADY_SPECIAL);
                    }
                    playerMapper.changePlayerPower(condition.getPlayerId(), '0', condition.getAppid());
                    log.info("用户操作已完成,赋权");
                    break;
                }
                case "unspec": {  //撤权
                    if (power.equals("1")) {
                        log.error("用户已是普通用户，无需再次撤权");
                        return new ResultMessage(ResponseEnum.ALEREADY_UNSPECIAL, null);
                    }
                    playerMapper.changePlayerPower(condition.getPlayerId(), '1', condition.getAppid());
                    log.info("用户操作已完成,撤权");
                    break;
                }

            }
        } catch (AppException e) {
            SpringRollBackUtil.rollBack();
            log.error("针对用户的操作失败,请检查....", new AppException(e.getCode(), e.getMessage()));
            return new ResultMessage(e.getCode(), e.getMessage(), null);
        }

        return new ResultMessage(ResponseEnum.SUCCESS, null);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyOptionMsg(String OperationMsg, Long operationId, Integer type) {
        if (type == 1) {
            try {
                operationMapper.changeOperationRecord(operationId, OperationMsg);
                log.info("操作原因修改成功");
            } catch (Exception e) {
                SpringRollBackUtil.rollBack();
                log.error("编辑操作原因失败,请检查....");
                throw new AppException(ResponseEnum.EDIT_OPERATION_MSG_FAILED);
            }

        }

        if (type == 2) {
            try {
                mobileDetailMapper.changeMobileByID(operationId, OperationMsg);
                log.info("设备名称修改成功");
            } catch (Exception e) {
                SpringRollBackUtil.rollBack();
                log.error("编辑设备名称失败,请检查....");
                throw new AppException(ResponseEnum.EDIT_DEVICE_NAME_FAILED);
            }
        }

    }


}
