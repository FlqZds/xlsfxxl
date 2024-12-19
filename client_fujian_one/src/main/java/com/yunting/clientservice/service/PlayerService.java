package com.yunting.clientservice.service;

import com.github.pagehelper.PageInfo;
import com.yunting.client.DTO.img.ImgContainer;
import com.yunting.common.Dto.PlayerDTO;
import com.yunting.common.results.ResultMessage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PlayerService {

    /***
     * 获取玩家红包信息
     * @param playerDTO  用户
     *
     */
    public String thisPlayerInRed(PlayerDTO playerDTO);


    /***
     * 十二点发请求 记录在线用户
     * @param playerDTO
     * @param location  位置信息
     *
     */
    void refreshOnline(PlayerDTO playerDTO, String location);


    /***
     * 获取玩家提现记录
     * @param playerDTO 玩家信息
     * @param packageName 游戏包名
     * @param pageNum 第几页
     * @return
     */
    public PageInfo getPlayerWithdrawRecord(PlayerDTO playerDTO, String packageName, Integer pageNum);


}
