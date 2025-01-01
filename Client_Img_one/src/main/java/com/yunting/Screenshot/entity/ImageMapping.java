package com.yunting.Screenshot.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageMapping {
    private Long imgId;

    private String imgUrl;

    private String directory;

    private String fileName;

    private String fileHash;

    private String imgType;

    private Long imgOrderId;

    private Long playerId;

    private LocalDateTime uploadTime;

    @ApiModelProperty("商户名")
    private String imgBusiness;
    @ApiModelProperty("充值金额")
    private String imgMoney;
    @ApiModelProperty(" 交易单号")
    private String imgTrans;
    @ApiModelProperty("商户单号")
    private String imgBusinessId;
    @ApiModelProperty("充值时间")
    private LocalDateTime imgPayTime;
    @ApiModelProperty("激励广告id")
    private String advEncourageId;

}