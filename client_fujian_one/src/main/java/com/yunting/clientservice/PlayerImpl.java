package com.yunting.clientservice;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yunting.client.DTO.VO.WithdrawVo;
import com.yunting.client.entity.*;
import com.yunting.client.entity.setting.GameSetting;
import com.yunting.client.mapper.Client.LocationMapper;
import com.yunting.client.mapper.Client.PlayerMapper;
import com.yunting.client.mapper.Client.WithdrawRecordMapper;
import com.yunting.client.mapper.DayBehaveRecordlistMapper;
import com.yunting.client.mapper.ImageMappingMapper;
import com.yunting.client.mapper.ImgorderMapper;
import com.yunting.clientservice.service.PlayerService;
import com.yunting.common.Dto.PlayerDTO;
import com.yunting.common.exception.AppException;
import com.yunting.common.results.ResponseEnum;
import com.yunting.common.results.ResultMessage;
import com.yunting.common.utils.RedisUtil_Record;
import com.yunting.common.utils.SpringRollBackUtil;
import com.yunting.forest.ForestService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;


@Slf4j
@Service("PlayerService")
@Data
public class PlayerImpl implements PlayerService {

    @Resource(name = "LocationMapper")
    private LocationMapper locationMapper;

    @Resource(name = "DayBehaveRecordlistMapper")
    private DayBehaveRecordlistMapper dayBehaveRecordlistMapper;

    @Resource(name = "PlayerMapper")
    private PlayerMapper playerMapper;

    @Resource(name = "ForestService")
    private ForestService forestService;

    @Resource(name = "WithdrawRecordMapper")
    private WithdrawRecordMapper withdrawMapper;


    @Resource(name = "ImageMappingMapper")
    ImageMappingMapper imageMappingMapper;

    @Resource(name = "ImgorderMapper")
    ImgorderMapper imgorderMapper;

    @Resource(name = "RedisUtil_Record")
    private RedisUtil_Record rur;


    @Override
    public void refreshOnline(PlayerDTO playerDTO, String location) {

    }

    /***
     * 获取用户提现记录
     * @param playerDTO 玩家信息
     * @return 用户提现记录
     */
    public PageInfo getPlayerWithdrawRecord(PlayerDTO playerDTO, String packageName, Integer pageNum) {
        Long playerId = playerDTO.getPlayerId();

        PageHelper.startPage(pageNum, 50);
        List<WithdrawVo> record = withdrawMapper.getWithdrawRecordByPlayerIdAndPackageName(playerId, packageName);
        PageInfo pageInfo = new PageInfo(record);

        log.info("用户提现记录已返回,第" + pageNum + "页的");
        return pageInfo;
    }


    @Override
    public String thisPlayerInRed(PlayerDTO playerDTO) {

        BigDecimal inred = playerMapper.selectInRedByPlayerId(playerDTO.getPlayerId(), playerDTO.getGameId());

        List<DayBehaveRecordlist> daybv = dayBehaveRecordlistMapper.getDayBehaveRecordlistByPlayerId(playerDTO.getPlayerId());
        DayBehaveRecordlist dayBehaveRecord = dayBehaveRecordlistMapper.getDayLastDayBehaveRecordlistByPlayerId(playerDTO.getPlayerId());

        BigDecimal todayred = BigDecimal.ZERO;
        BigDecimal totalRed = BigDecimal.ZERO;
        if (dayBehaveRecord == null || daybv == null) {
            todayred = BigDecimal.ZERO;
            totalRed = BigDecimal.ZERO;
        } else {
            dayBehaveRecord.getTodayred();

            for (DayBehaveRecordlist d : daybv) {
                totalRed = totalRed.add(d.getTotalred());
            }

        }

        HashMap<String, Object> json_map = new HashMap<>();
        json_map.put("inred", inred);
        json_map.put("totalRed", totalRed);
        json_map.put("todayred", todayred);
        String s = JSON.toJSONString(json_map);
        return s;
    }

}
