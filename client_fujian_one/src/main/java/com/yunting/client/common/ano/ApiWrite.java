package com.yunting.client.common.ano;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
        @ApiResponse(code = 200, message = "请求成功"),
        @ApiResponse(code = 500, message = "请求失败"),
        @ApiResponse(code = 404, message = "未存在的资源请求"),
        @ApiResponse(code = 401, message = "未身份验证的请求"),
        @ApiResponse(code = 403, message = "未授权的请求"),

})
public @interface ApiWrite {
}
