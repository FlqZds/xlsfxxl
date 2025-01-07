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

    //图片订单状态
    public static final String IMG_TYPE = ".jpeg";

    //"支付宝订单流水排头"
    public static String Pay_Order_Header = "ythylyxxl";


    //激励广告类型
    public static final String NormalEnc = "正常激励";
    public static final String WithDrawEnc = "提现激励";
    public static final String OrderEnc = "订单激励";

    //标识激励广告已经看完
    public static final String IsCloseEnc = "0";

    //服务端发送心跳的间隔，单位为minus
    public static final int serverHeartBearDuration = 1;

    //服务端心跳检测消息
    public static final String heartBeatMsg = "ping";
    //客户端心跳回应消息
    public static final String heartBeatRespMsg = "pong";
    public static final String GATH_DETECTED_MSG = ":IS Judged";

    public static final String ADN_csj = "穿山甲";
    public static final String ADN_ks = "快手";
    public static final String ADN_bd = "百度";
    public static final String ADN_ylh = "优量汇";


    //服务端检测连接状态的时间间隔，单位为minus
    public static final int serverClearSessionInterval = 2;

    public static final String PROPERTIES_PATH = "D:\\a-share\\application.properties";
    public static final String XML_PATH = "D:\\a-share\\logback.xml";


    public static final BigDecimal limitRed = new BigDecimal("0.3"); //最小提现金额
    public static final BigDecimal MAXRed = new BigDecimal("200.00");   //最大提现金额
    public static final BigDecimal Percentage = new BigDecimal("0.01");   //百分比 | 用户余额小于该值,提示余额不足
}
