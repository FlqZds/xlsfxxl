package com.yunting.common.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExceptionRecordlsit {
    private Long exceptionId;

    private String wxNickName;

    private String playerId;

    private String gameId;

    private String oaid;
    private String androidId;
    private String deviceType;
    private String deviceDetail;

    private String exceptType;

    private String exceptInfo;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") //此注解用来接收字符串类型的参数封装成LocalDateTime类型
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8", shape = JsonFormat.Shape.STRING) //此注解将date类型数据转成字符串响应出去
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)		// 反序列化
    @JsonSerialize(using = LocalDateTimeSerializer.class)		// 序列化
    private LocalDateTime exceptionTime;

}