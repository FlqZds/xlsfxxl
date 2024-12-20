package com.yunting.common.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class Gst {
    //用户聚集限制设置
    private static boolean Gather_Choice = true; //聚集选择项 true是小范围,false是大范围
    private static int Gather_Big_Limit = 10;   //聚集大范围限制人数
    private static int Gather_Small_Limit = 10; //聚集小范围限制人数

    private static int Same_Model_Limit = 10;   //聚集手机同型号限制的设备数量
    private static int Same_Mac_Limit = 10;     //允许同一个mac地址在线用户数量

    /***
     * 仅供内部
     * 在管理端聚集限制设置修改了之后
     * 提示这里更新全局相关的数据
     */
    public static void changeGlogbal_Gathering_Setting(boolean Gather_Choice, int Gather_Big_Limit, int Gather_Small_Limit, int Same_Model_Limit, int Same_Mac_Limit) {
        Gst.Gather_Choice = Gather_Choice;
        Gst.Gather_Big_Limit = Gather_Big_Limit;
        Gst.Gather_Small_Limit = Gather_Small_Limit;
        Gst.Same_Model_Limit = Same_Model_Limit;
        Gst.Same_Mac_Limit = Same_Mac_Limit;
        log.info("全局聚集设置已更新");
    }

    public static boolean isGather_Choice() {
        return Gather_Choice;
    }

    public static int getGather_Big_Limit() {
        return Gather_Big_Limit;
    }

    public static int getGather_Small_Limit() {
        return Gather_Small_Limit;
    }

    public static int getSame_Model_Limit() {
        return Same_Model_Limit;
    }

    public static int getSame_Mac_Limit() {
        return Same_Mac_Limit;
    }
}
