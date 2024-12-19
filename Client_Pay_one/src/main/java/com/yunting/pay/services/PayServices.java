package com.yunting.pay.services;

import com.yunting.common.results.ResultMessage;

public interface PayServices {

    public ResultMessage applyWithdraw(Long playerId, String payId, String realName);

    public Integer manIdentify(Long playerId, String payId, String realName);
}
