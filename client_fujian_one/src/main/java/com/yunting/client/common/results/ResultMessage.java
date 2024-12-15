package com.yunting.client.common.results;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "返回结果")
public class ResultMessage<T> {

    @ApiModelProperty(value = "响应状态码", example = "401")
    private String code;

    @ApiModelProperty(value = "消息", example = "未身份验证的请求")
    private String message;

    @ApiModelProperty(value = "携带的数据", example = "null")
    private T data;

    public ResultMessage(ResponseEnum responseEnum, T data) {
        this.code = responseEnum.getCode();
        this.message = responseEnum.getMessage();
        this.data = data;
    }
}
