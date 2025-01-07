package com.yunting.common.utils;

import org.springframework.stereotype.Component;

import java.time.*;

@Component("TimeUtils")
public class TimeUtils {

    /***
     * 获取当天剩余时间(秒)
     */
    public static long ONE_DAY_MILLISECONDS() {
        return LocalDateTime.now().until(LocalDateTime.now().plusDays(1).withHour(00).withMinute(00).withSecond(00),
                java.time.temporal.ChronoUnit.SECONDS);
    }


    /***
     * 获取0点到当前时间的时间戳
     * @return
     */
    public static long getTimeStamp() {

        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();

        // 获取当天的0点时间
        LocalDateTime midnight = now.toLocalDate().atStartOfDay();

        // 将LocalDateTime转换为ZonedDateTime
        ZonedDateTime zonedDateTimeNow = now.atZone(ZoneId.systemDefault());
        ZonedDateTime zonedDateTimeMidnight = midnight.atZone(ZoneId.systemDefault());

        // 计算时间戳差值（毫秒）
        long timeDifference = zonedDateTimeNow.toInstant().toEpochMilli() - zonedDateTimeMidnight.toInstant().toEpochMilli();

        // 输出结果
        return timeDifference;
    }

    /***
     * 获取从0点到指定时间的时间戳
     * @return
     */
    public static long getThisTimeStamp(int hour, int minute) {
        LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(hour, minute));

        // 获取当天的0点时间
        LocalDateTime midnight = localDateTime.toLocalDate().atStartOfDay();

        // 将LocalDateTime转换为ZonedDateTime
        ZonedDateTime zonedDateTimeNow = localDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime zonedDateTimeMidnight = midnight.atZone(ZoneId.systemDefault());

        // 计算时间戳差值（毫秒）
        long timeDifference = zonedDateTimeNow.toInstant().toEpochMilli() - zonedDateTimeMidnight.toInstant().toEpochMilli();
        return timeDifference;
    }

    public static int getDayOfWeek() {
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        int value = dayOfWeek.getValue();
//        now
        return value;
    }
}
