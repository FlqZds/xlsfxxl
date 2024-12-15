package com.yunting.client.DTO.img;

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
@ApiModel(value = "图片信息", description = "传图片信息的")
public class ImgVo {
    @ApiModelProperty("图片名称")
    private String imgName;
    @ApiModelProperty("图片hash值")
    private String imgHash;
}
