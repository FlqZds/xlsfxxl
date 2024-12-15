package com.yunting;

import com.yunting.client.Ws.Wso;
import com.yunting.client.common.utils.RedisUtils_Wlan;
import com.yunting.client.mapper.Client.LocationMapper;
import com.yunting.client.mapper.Client.PlayerMapper;
import com.yunting.client.mapper.DayBehaveRecordlistMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class MyInit extends AbstractMessageSource implements ResourceLoaderAware {
    
    @PostConstruct
    public void init() {
        MyInit myInit = this;
        this.DailyTask();

    }

    //    维护一个单一线程池    (十二点)
//    清理redis
//    批量插入玩家记录
    private HashSet<Long> playerIDSets;

    public void DailyTask() {
        //        计算当前时间距离第二天凌晨00:00的秒数
        Long second = LocalDateTime.now().until(LocalDateTime.now().plusDays(1).withHour(00).withMinute(00).withSecond(00),
                java.time.temporal.ChronoUnit.SECONDS);

//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                log.info("清理redis中");
//            }
//        });

        List<Long> playersID = playerMapper.selectPlayersByGameId(123L);
        for (Long l : playersID) {
            if (dayBehaveMapper.getDayLastDayBehaveRecordlistByPlayerId(l) == null) {
                playerIDSets.add(l);  //要批量插入空留存数据的玩家id
            }
        }


////        定时任务线程池
//        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);
//        scheduledThreadPool.scheduleWithFixedDelay(thread,1,10, TimeUnit.SECONDS);
    }

    @Resource(name = "PlayerMapper")
    PlayerMapper playerMapper;
    @Resource(name = "LocationMapper")
    LocationMapper locationMapper;
    @Resource(name = "DayBehaveRecordlistMapper")
    DayBehaveRecordlistMapper dayBehaveMapper;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {

    }

    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        return null;
    }
}
