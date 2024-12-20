package com.yunting.common.utils;


import com.yunting.common.Dto.RiskControlSetting;
import lombok.extern.slf4j.Slf4j;

/***
 * 该游戏全局设置
 */
@Slf4j
public final class ST {

    //全局常量字段
    private static String APPID = "wx29d88fe25fd2b103";
    private static String APPSECRECT = "c6aeb37fd7e0b4f2bbe05d628e401831";
    private static String GAME_ID = "12419";
    private static String PACKAGE_NAME = "com.layabox.gameyzzs";

    /***
     * 仅供内部
     * 在管理端常量数据修改了之后
     * 提示这里更新全局相关的数据
     */
    public static void changeGameVal(String APPID, String APPSECRECT, String GAME_ID, String PACKAGE_NAME) {
        ST.APPID = APPID;
        ST.APPSECRECT = APPSECRECT;
        ST.GAME_ID = GAME_ID;
        ST.PACKAGE_NAME = PACKAGE_NAME;
        log.info("全局常量字段已更新");
    }

    //全局杂项设置 字段 (后端)
    private static boolean Retain_Way = true;  //留存方式 (true是自然日,false是24小时)
    private static int Daily_Active_Level = 12;//每日活跃标准

    private static int Reset_Max_Time = 12; //应用后台最大闲置时长(分钟)
    private static boolean IS_Weekend = true; //周末是否限制时间段

    private static boolean Forbid_Switch = true; //禁止观看广告时间段的开关
    private static int Forbid_Begin_Time = 12; //禁止观看广告的开始时间段
    private static int Forbid_End_Time = 12; //禁止观看广告的结束时间段

    //客户端
    private static int Min_System_Version = 0;   //设备系统低于以下禁止登录
    private static int ADV_Interval = 0;   //请求广告与收下奖励的时间间隔
    private static String Notification = "114514"; //公告

    /***
     * 仅供内部
     * 在管理端全局其他设置修改了之后
     * 提示这里更新全局相关的数据
     */
    public static void changeNotification(String notification) {
        ST.Notification = notification;
        log.info("通知已更新");
    }

    /***
     * 仅供内部
     * 在管理端全局其他设置修改了之后
     * 提示这里更新全局相关的数据
     */
    public static void changeRedunt(boolean retain_Way, int daily_Active_Level, int reset_Max_Time, boolean is_Weekend, boolean forbid_Switch, int forbid_Begin_Time, int forbid_End_Time, int adv_Interval, int min_System_Version) {
        ST.Retain_Way = retain_Way;
        ST.Daily_Active_Level = daily_Active_Level;

        ST.Reset_Max_Time = reset_Max_Time;
        ST.IS_Weekend = is_Weekend;

        ST.Forbid_Switch = forbid_Switch;
        ST.Forbid_Begin_Time = forbid_Begin_Time;
        ST.Forbid_End_Time = forbid_End_Time;

        ST.ADV_Interval = adv_Interval;
        ST.Min_System_Version = min_System_Version;
        log.info("全局他设置设置已更新");
    }


    //用户奖励比例设置
    private static int ADV_Percent = 200;      //用户广告比例（%）
    private static int Reward_Limit = 1000; //可领取奖励上限

    /***
     * 仅供内部
     * 在管理端用户奖励比例设置修改了之后
     * 提示这里更新全局相关的数据
     */
    public static void changeReward(int ADV_Percent, int Reward_Limit) {
        ST.ADV_Percent = ADV_Percent;
        ST.Reward_Limit = Reward_Limit;
        log.info("全局用户奖励比例设置已更新");
    }


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
    public static void changeGath(boolean Gather_Choice, int Gather_Big_Limit, int Gather_Small_Limit, int Same_Model_Limit, int Same_Mac_Limit) {
        ST.Gather_Choice = Gather_Choice;
        ST.Gather_Big_Limit = Gather_Big_Limit;
        ST.Gather_Small_Limit = Gather_Small_Limit;
        ST.Same_Model_Limit = Same_Model_Limit;
        ST.Same_Mac_Limit = Same_Mac_Limit;
        log.info("全局聚集设置已更新");
    }

    //截图设置
    private static boolean Shot_Switch = true;       //是否开启截图设置
    private static double Codebit_Max_val = 200;       //代码位ecpm值大于....
    private static int Daily_Max_Submit_Num = 21; //当日允许用户提交截图订单上限
    private static int Daily_Max_Watch_Num = 12; //领取奖励观看广告次数


    /***
     * 仅供内部
     * 在管理端截图设置修改了之后
     * 提示这里更新全局相关的数据
     */
    public static void changeScreenshot(boolean Shot_Switch, double Codebit_Max_val, int Daily_Max_Submit_Num, int Daily_Max_Watch_Num) {
        ST.Shot_Switch = Shot_Switch;
        ST.Codebit_Max_val = Codebit_Max_val;
        ST.Daily_Max_Submit_Num = Daily_Max_Submit_Num;
        ST.Daily_Max_Watch_Num = Daily_Max_Watch_Num;
        log.info("全局截图设置已更新");
    }


    //风控设置
    private static RiskControlSetting Risk = RiskControlSetting.builder()
            .packageName(PACKAGE_NAME)
            .rootEnable('1')
            .noBarrier('1')
            .usb('1')
            .otg('1')
            .adb('1')
            .bluetooth('1')
            .hotdot('1')
            .charge('1')
            .vpn('1')
            .sim('1')
            .simulator('1')
            .build();

    /***
     * 仅供内部
     * 在管理端风控设置修改了之后
     * 提示这里更新全局相关的数据
     */
    public static void changeRisk(RiskControlSetting riskControlSetting) {
        Risk = new RiskControlSetting(riskControlSetting);
        log.info("全局风控设置已更新");
    }


    //提现设置
    private static String Withdraw_Nojudge_Money = "0";//用户免审核提现金额
    private static String Withdraw_Percentage = "0";   //提现比例
    private static boolean Daily_Withdraw_Switch = false;       //每日提现次数 功能开关
    private static int Daily_Withdraw_Count = 0;       //每日提现次数

    /***
     * 仅供内部
     * 在管理端风控设置修改了之后
     * 提示这里更新全局相关的数据
     */
    public static void changeWithdraw(String Withdraw_Nojudge_Money, String Withdraw_Percentage, boolean Daily_Withdraw_Switch, int Daily_Withdraw_Count) {
        ST.Withdraw_Nojudge_Money = Withdraw_Nojudge_Money;
        ST.Withdraw_Percentage = Withdraw_Percentage;
        ST.Daily_Withdraw_Switch = Daily_Withdraw_Switch;
        ST.Daily_Withdraw_Count = Daily_Withdraw_Count;
        log.info("全局提现设置已更新");
    }


    public static String APPID() {
        return APPID;
    }

    public static String APPSECRECT() {
        return APPSECRECT;
    }

    /***
     * 游戏id
     * @return
     */
    public static String GameId() {
        return GAME_ID;
    }

    /***
     * 包名
     * @return
     */
    public static String PackageName() {
        return PACKAGE_NAME;
    }

    public static boolean isRetain_Way() {
        return Retain_Way;
    }

    public static int Daily_Active_Level() {
        return Daily_Active_Level;
    }

    /***
     * 应用后台最大闲置时长
     */
    public static int Reset_Max_Time() {
        return Reset_Max_Time;
    }

    public static boolean isIS_Weekend() {
        return IS_Weekend;
    }

    public static boolean isForbid_Switch() {
        return Forbid_Switch;
    }

    public static int Forbid_Begin_Time() {
        return Forbid_Begin_Time;
    }

    /***
     * 禁止看广告的结束时间
     */
    public static int Forbid_End_Time() {
        return Forbid_End_Time;
    }

    /***
     * 设备系统低于以下禁止登录
     */
    public static int getMin_System_Version() {
        return Min_System_Version;
    }

    public static int ADV_Interval() {
        return ADV_Interval;
    }

    public static String Notification() {
        return Notification;
    }

    /***
     * 用户奖励比例
     */
    public static int ADV_Percent() {
        return ADV_Percent;
    }

    /***
     * 用户奖励上限
     */
    public static int Reward_Limit() {
        return Reward_Limit;
    }

    public static boolean isGather_Choice() {
        return Gather_Choice;
    }

    public static int Gather_Big_Limit() {
        return Gather_Big_Limit;
    }

    public static int Gather_Small_Limit() {
        return Gather_Small_Limit;
    }

    /***
     * 同型号手机限制设备数量
     * @return
     */
    public static int Same_Model_Limit() {
        return Same_Model_Limit;
    }

    /***
     * 同mac地址限制设备数量
     * @return
     */
    public static int Same_Mac_Limit() {
        return Same_Mac_Limit;
    }

    /***
     * 是否开启截图设置
     * @return
     */
    public static boolean isShot_Switch() {
        return Shot_Switch;
    }

    /***
     * 代码位ecpm值大于....
     * @return
     */
    public static double Codebit_Max_val() {
        return Codebit_Max_val;
    }

    /***
     * 当日允许用户提交截图订单上限
     * @return
     */
    public static int Daily_Max_Submit_Num() {
        return Daily_Max_Submit_Num;
    }

    /***
     * 领取奖励观看广告次数
     * @return
     */
    public static int Daily_Max_Watch_Num() {
        return Daily_Max_Watch_Num;
    }

    /***
     * 用户免审核提现金额
     * @return
     */
    public static String Withdraw_Nojudge_Money() {
        return Withdraw_Nojudge_Money;
    }

    /***
     * 提现比例
     * @return
     */
    public static String Withdraw_Percentage() {
        return Withdraw_Percentage;
    }

    /***
     * 每日提现次数 功能开关
     * @return
     */
    public static boolean isDaily_Withdraw_Switch() {
        return Daily_Withdraw_Switch;
    }

    /***
     * 每日提现次数
     * @return
     */
    public static int Daily_Withdraw_Count() {
        return Daily_Withdraw_Count;
    }
}
