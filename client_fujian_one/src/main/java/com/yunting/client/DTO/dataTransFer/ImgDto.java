package com.yunting.client.DTO.dataTransFer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "图片信息载体", description = "传图片信息的")
public class ImgDto {
    @ApiModelProperty("图片请求路径")
    private String imgUrl;
    @ApiModelProperty("上传者wx昵称")
    private String imgName;
    @ApiModelProperty("上传者玩家id")
    private String imgPlayerID;
    @ApiModelProperty("订单号")
    private String imgOrder;
    @ApiModelProperty("文件类型")
    private String imgType;
    @ApiModelProperty("文件上传来源(来自哪个游戏)")
    private String imgOrigin;
    @ApiModelProperty("上传者安卓id")
    private String oaid;
    @ApiModelProperty("上传文件名称")
    private String imgFileName;
    @ApiModelProperty("文件上传时间")
    private String uploadTime;
}
