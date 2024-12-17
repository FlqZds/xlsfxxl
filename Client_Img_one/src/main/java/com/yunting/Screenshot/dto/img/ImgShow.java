package com.yunting.Screenshot.dto.img;

import io.swagger.annotations.ApiModel;
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
@ApiModel(value = "图片", description = "图片")
public class ImgShow {
    @ApiModelProperty("图片的名称")
    private String fileName;
    @ApiModelProperty("该图片的hash")
    private String fileHash;
    @ApiModelProperty("该组图片是哪一组的,dir相同的为一组")
    private String directory;
    @ApiModelProperty("该组图片的上传时间")
    private LocalDateTime uploadTime;
}
