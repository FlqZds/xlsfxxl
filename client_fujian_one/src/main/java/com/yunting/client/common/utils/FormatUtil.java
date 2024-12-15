package com.yunting.client.common.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class FormatUtil {

    /***
     * 将时间字符串转换为LocalDate
     * @param rowDateJson 原始时间字符串
     * @return
     */
    public static LocalDate getLocalDate(String rowDateJson){

        // ISO 8601格式的时间字符串
        String isoDateTime = rowDateJson;

        // 解析ISO 8601格式的时间字符串为Instant对象
        Instant instant = Instant.parse(isoDateTime);

        // 获取本地时区
        ZoneId zoneId = ZoneId.systemDefault();

        // 将Instant转换为本地时区的ZonedDateTime
        ZonedDateTime zonedDateTime = instant.atZone(zoneId);

        // 打印ZonedDateTime
//        System.out.println("ZonedDateTime: " + zonedDateTime);

        // 如果需要LocalDateTime（没有时区信息）
        LocalDate localDate = zonedDateTime.toLocalDate();

        return localDate;
    }
}
