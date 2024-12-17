package com.yunting.adminservice;

import com.github.pagehelper.PageInfo;
import com.yunting.client.DTO.RetainActive;
import com.yunting.client.DTO.incondition;
import com.yunting.common.results.ResultMessage;

import java.io.IOException;

public interface AdminService {

    /***
     * 查询该应用的所有操作记录
     *
     */
    public PageInfo queryAllMobileInfo(RetainActive active);

    /***
     * 查询该应用的所有操作记录
     *
     */
    public PageInfo queryAllOperation(RetainActive active);


    /***
     * 查询该应用的所有异常记录
     *
     */
    public PageInfo queryAllException(RetainActive active);


    /***
     * 根据给定条件查询该应用该用户 某个详细信息
     * @param appid 应用
     * @param playerId 用户
     * @param condition 给定的查询条件
     * @param page 给定的页码
     * @param recordTime 给定的比较时间
     */
    public PageInfo queryPlayerAnyDetailByCondition(String appid,Long playerId,String condition,Integer page,String recordTime);


    /***
     * 根据给定条件查询该应用所有用户的留存活跃
     * @param retainActive 留存活跃对象
     */
    public PageInfo queryRetainActive(RetainActive retainActive);



    /***
     * 给管理后台发 玩家的信息
     * @param active 查询条件
     *
     */
    public PageInfo sendPlayerInfo(RetainActive active);

    /***
     * 操作用户  (封禁 解封  设置特殊用户 解除特殊用户)
     * @return
     */
    public ResultMessage changeUserPower(incondition incondition) throws IOException;

    /***
     * 修改操作记录的原因
     * @param OperationMsg 原因
     * @param exceptionId  要修改的记录ID
     */
    public void modifyOptionMsg(String OperationMsg,Long exceptionId,Integer type);




}
