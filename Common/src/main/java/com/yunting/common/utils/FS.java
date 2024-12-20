package com.yunting.common.utils;

import java.math.BigDecimal;

/***
 * 常量类
 * @author ：zds
 *仅FS能用，不可被继承，不可被外界改变
 */
public final class FS {
    public static String temp_code = "";
    public static String temp_msg = "";

    //订单图片类型
    public static final String Order_Status = "1A";
    //图片订单状态
    public static final String IMG_TYPE = ".jpeg";

    //"支付宝订单流水排头"
    public static String Pay_Order_Header = "ythylyxxl";

    public static final String SECRET_KEY_PUBLIC = "PUBLIC_KEY";
    public static final String SECRET_KEY_PRIVATE = "PRIVATE_KEY";

    //    管理端默认登录头像
    public final static String DEFAULT_AVATAR = "https://zdsflq.oss-cn-beijing.aliyuncs.com/avatar/%E9%BB%98%E8%AE%A4%E5%A4%B4%E5%83%8F.svg";

    //swagger 客户端组默认标题
    public final static String CLient_Title = "客户端对接-API文档";
    public final static String CLient_Description = "本文档描述了对接c端的接口基本概述";

    //swagger 管理端组默认标题
    public final static String Admin_Title = "管理端对接-API文档";
    public final static String Admin_Description = "本文档描述了对接c端的接口基本概述";

    //swagger 管理端组默认标题
    public final static String AdvRecord_Title = "广告记录对接-API文档";
    public final static String AdvRecord_Description = "本文档描述了对接c端的广告记录接口的基本概述";

    //服务端发送心跳的间隔，单位为minus
    public static final int serverHeartBearDuration = 1;

    //服务端心跳检测消息
    public static final String heartBeatMsg = "ping";
    //客户端心跳回应消息
    public static final String heartBeatRespMsg = "pong";
    public static final String GATH_DETECTED_MSG = ":IS Judged";


    //服务端检测连接状态的时间间隔，单位为minus
    public static final int serverClearSessionInterval = 2;

    public static final String PROPERTIES_PATH = "D:\\a-share\\application.properties";
    public static final String XML_PATH = "D:\\a-share\\logback.xml";


    public static final BigDecimal limitRed = new BigDecimal("0.3"); //最小提现金额
    public static final BigDecimal MAXRed = new BigDecimal("200.00");   //最大提现金额
    public static final BigDecimal Percentage = new BigDecimal("0.01");   //百分比 | 用户余额小于该值,提示余额不足
}
