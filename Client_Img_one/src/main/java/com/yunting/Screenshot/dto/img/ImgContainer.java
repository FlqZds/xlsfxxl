package com.yunting.Screenshot.dto.img;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "信息", description = "信息")
public class ImgContainer {
    @ApiModelProperty("要上传的图片类型")
    String itp;
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
    @ApiModelProperty("图片信息")
    List<ImgVo> imgVos;
}
