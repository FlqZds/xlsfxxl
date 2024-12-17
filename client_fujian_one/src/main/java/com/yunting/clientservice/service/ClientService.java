package com.yunting.clientservice.service;

import com.yunting.client.DTO.dataTransFer.PlayerMetaData;
import com.yunting.client.entity.setting.RiskControlSetting;
import com.yunting.client.entity.setting.UserRewardSetting;
import com.yunting.common.Dto.PlayerDTO;

public interface ClientService {


    /***
     * 获取当前的游戏设置
     *
     */
    public PlayerMetaData getGameSetting(PlayerDTO playerDTO, String packageName);

    /***
     * 通过应用包名获取当前的用户奖励设置
     * @param packageName  应用包名
     */
    public UserRewardSetting getUserRewardSetting(String packageName);


    /***
     * 通过应用包名获取当前的风控参数设置
     *
     */
    public RiskControlSetting getRiskControlSetting(String packageName);

//    public DailyTaskDTO getDailyTask(Integer daily_task_adv_count, Integer daily_task_count, String app_id);


}
