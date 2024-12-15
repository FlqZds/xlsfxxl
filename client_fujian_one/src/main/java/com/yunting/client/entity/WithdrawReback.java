package com.yunting.client.entity;

import java.math.BigDecimal;

public class WithdrawReback {
    private Integer withdrawRebackId;

    private Double withdrawRebackPercentage;

    private BigDecimal withdrawMoney;

    private Long gameId;

    public Integer getWithdrawRebackId() {
        return withdrawRebackId;
    }

    public void setWithdrawRebackId(Integer withdrawRebackId) {
        this.withdrawRebackId = withdrawRebackId;
    }

    public Double getWithdrawRebackPercentage() {
        return withdrawRebackPercentage;
    }

    public void setWithdrawRebackPercentage(Double withdrawRebackPercentage) {
        this.withdrawRebackPercentage = withdrawRebackPercentage;
    }

    public BigDecimal getWithdrawMoney() {
        return withdrawMoney;
    }

    public void setWithdrawMoney(BigDecimal withdrawMoney) {
        this.withdrawMoney = withdrawMoney;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }
}