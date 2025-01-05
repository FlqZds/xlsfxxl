package com.yunting.ws.service;

import com.yunting.common.results.ResultMessage;
import com.yunting.ws.entity.incondition;

import java.io.IOException;

public interface AdminService {


    /***
     * 操作用户  (封禁 解封  设置特殊用户 解除特殊用户)
     * @return
     */
    public ResultMessage changeUserPower(incondition incondition) throws IOException;


}
