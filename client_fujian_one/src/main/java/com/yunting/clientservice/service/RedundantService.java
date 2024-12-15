package com.yunting.clientservice.service;

import com.yunting.client.DTO.PlayerDTO;

import java.util.List;

public interface RedundantService {
    /***
     * 获取到所有游戏的列表,除开他自己
     */
    public List getMoreEntertainment();

    /***
     * 下载该游戏
     */
    public void download(PlayerDTO playerDTO);
}
