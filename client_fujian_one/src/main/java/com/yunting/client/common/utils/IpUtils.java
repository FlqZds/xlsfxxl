package com.yunting.client.common.utils;

import com.dtflys.forest.utils.StringUtils;
import com.yunting.SpringBeanContext;
import com.yunting.forest.ForestService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.lionsoul.ip2region.Util;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;


@Slf4j
public class IpUtils {

    /**
     * 本地环回地址
     */
    private static final String LOCAL_IP = "127.0.0.1";
    public static final String REQUEST_URL = "https://httpbin.org/ip";
    private static final String PRE_REQUEST_URL = "https://ipecho.net/plain";

    /**
     * 未知
     */
    private static final String UNKNOWN = "unknown";


    /***
     *
     * 拿到用户的ip
     * @param request 发过来的请求
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {

        if (request == null) {
            return UNKNOWN;
        }

        String host = request.getHeader("host");

        //判断这个ip是否为代理ip / 从各种代理中获取到ip
        String ip = request.getHeader("x-forwarded-for");

        // 通过Apache代理获取ip
        if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        // 通过nginx获取ip
        if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        // 通过Nginx获取ip（Nginx中的另一个变量，内容就是请求中X-Forwarded-For的信息）
        if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        // 通过WebLogic代理获取ip
        if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        //兼容集群获取ip
        if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // 客户端和服务器为同一台机器时，获取的地址为IPV6格式："0:0:0:0:0:0:0:1"
        // 最后在判断该ip 是否是本地环回地址 若不是 则返回ip，否则就是 127.0.0.1 localhost返回
        return "0:0:0:0:0:0:0:1".equals(ip) ? LOCAL_IP : ip;
    }


    private static final String DB_PATH = "C:\\Users\\Administrator\\Desktop\\Client\\src\\main\\resources\\ip2region.db";
    private static final String DEFAULT_CITY_NAME = "未知地址";


    private static ForestService forest;

    /**
     * 获取ip
     *
     * @return
     */
    public static String getIp() {
        forest = SpringBeanContext.getContext().getBean("ForestService", ForestService.class);
        String ip = forest.getIp();
        if (!Util.isIpAddress(ip) || ip.length() <= 3 || ip == null) {
            ip = forest.getIp_temp();
        }

//        "origin": "220.200.34.50"
        //将该字符串拆分为ip
        String[] split = ip.split(":");
        String s = split[split.length - 1];
        s = s.replace("}", "");
        //去掉首尾的"
        String s1 = s.replace("\"", "");
        //去掉s1的空格
        return s1.trim();

    }


    public static String getCityInfo(String ip) {

        if (!Util.isIpAddress(ip)) { //字符串不符合ip格式则直接返回
            log.error("错误: 无效的ip地址");
            return "无效的ip地址|已配置国外代理";
        }

        //解析资源文件为 流
        InputStream is = null;
        File target = null;
        try {
            is = new PathMatchingResourcePatternResolver().getResources("ip2region.db")[0].getInputStream();

            //从Resource类路径中定位并获取名为ip2region.db的资源文件。
            //将该资源文件复制到当前工作目录下
            target = new File("ip2region.db");
            FileUtils.copyInputStreamToFile(is, target);
            is.close();
        } catch (IOException e) {
            log.warn("读取省份数据库失败,请检查相关情况" + e);
        }

        if (StringUtils.isEmpty(String.valueOf(target))) {
            log.error("错误: 无效的ip2region.db文件");
            return null;
        }

        DbSearcher searcher = null;//ip2region的核心处理类
        try {
            searcher = new DbSearcher(new DbConfig(), String.valueOf(target));
        } catch (Exception e) {
            log.warn("ip2region创建searcher对象失败,请检查相关" + e);
        }

        try {
            DataBlock dataBlock = (DataBlock) searcher.getClass().getMethod("btreeSearch", String.class).invoke(searcher, ip);

            String ipInfo = dataBlock.getRegion();
            if (!StringUtils.isEmpty(ipInfo)) {
                ipInfo = ipInfo.replace("|0", "");
                ipInfo = ipInfo.replace("0|", "");
            }

            return ipInfo;
        } catch (Exception e) {
            log.warn("ip2region查询失败,请检查相关" + e);
        }
        return null;
    }


}