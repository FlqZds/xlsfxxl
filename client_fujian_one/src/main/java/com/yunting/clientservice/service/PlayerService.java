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

    /***
     * 获取该玩家未上传的截图
     * @param playerDTO 玩家信息
     * @param imgType 哪种截图
     * @return list<ImgShow> 未上传截图的文件信息
     */
    public List getThisPlayerPreUploadImg(PlayerDTO playerDTO, String imgType);


    /***
     * 上传图片|文件
     * 校验hash值
     * @param playerDTO 玩家信息
     * @param androidID 安卓id
     */
    public ResultMessage uploadImgByPlayerAndAddOrder(PlayerDTO playerDTO, String androidID, List<MultipartFile> files) throws IOException;

    /***
     * 上传文件之前的预处理
     * 文件名称和hash值,等相关信息上传
     * @param playerDTO 玩家信息
     * @param imgContainer 多个图片的信息
     */
    public void preUploadFileNameAndHashVal(PlayerDTO playerDTO, ImgContainer imgContainer) throws IOException;

}
