package com.yunting.common.utils;


import com.yunting.common.Dto.RiskControlSetting;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;


/***
 * 该游戏全局设置
 */
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public final class ST {
    public int broadCount = 0;

    @Resource(name = "RedisUtil_Record")
    private RedisUtil_Record rur;

    //全局常量字段
    private String APPID = "wx29d88fe25fd2b103";
    private String APPSECRECT = "c6aeb37fd7e0b4f2bbe05d628e401831";
    private String GAME_ID = "12419";
    private String PACKAGE_NAME = "com.layabox.gameyzzs";
    private String DB_Name = "yunting_app_test";


    //风控设置
    public RiskControlSetting Risk() {
        RiskControlSetting risk = new RiskControlSetting();
        risk.setPackageName(this.PACKAGE_NAME);

        risk.setBluetooth(rur.hGet(this.GAME_ID, "bluetooth").toString());
        risk.setHotdot(rur.hGet(this.GAME_ID, "hotdot").toString());
        risk.setCharge(rur.hGet(this.GAME_ID, "charge").toString());
        risk.setVpn(rur.hGet(this.GAME_ID, "vpn").toString());

        risk.setUsb(rur.hGet(this.GAME_ID, "usb").toString());
        risk.setOtg(rur.hGet(this.GAME_ID, "otg").toString());
        risk.setAdb(rur.hGet(this.GAME_ID, "adb").toString());

        risk.setNoBarrier(rur.hGet(this.GAME_ID, "no_barrier").toString());
        risk.setRootEnable(rur.hGet(this.GAME_ID, "root_enable").toString());
        risk.setSim(rur.hGet(this.GAME_ID, "sim").toString());
        risk.setSimulator(rur.hGet(this.GAME_ID, "simulator").toString());

        return risk;
    }

    private RiskControlSetting Risk = RiskControlSetting.builder().build();


    public String APPID() {
        return APPID;
    }

    public String APPSECRECT() {
        return APPSECRECT;
    }


    /***
     * 游戏对应数据库名称
     *
     */
    public String DB_Name() {
        return DB_Name;
    }


    /***
     * 游戏id
     *
     */
    public String GameId() {
        return GAME_ID;
    }

    /***
     * 包名
     *
     */
    public String PackageName() {
        return PACKAGE_NAME;
    }

    /***
     * 留存方式
     * <p>
     * 0=true是自然日
     * <p>
     * 1=false是24hours
     */
    public boolean Retain_Way() {
        return rur.getBit("Retain_Way", Long.parseLong(this.GAME_ID));
    }

    public int Daily_Active_Level() {
        return Integer.parseInt(rur.get("Daily_Active_Level"));
    }

    /***
     * 应用后台最大闲置时长
     */
    public int Reset_Max_Time() {
        return Integer.parseInt(rur.get("Reset_Max_Time"));
    }

    /***
     * 周末是否闲置时间段
     *  true | false
     */
    public boolean IS_Weekend() {
        return rur.getBit("isIS_Weekend", Long.parseLong(this.GAME_ID));
    }

    /***
     * 是否 禁止观看广告时间的时间段
     *  true | false
     */
    public boolean isForbid_Switch() {
        return rur.getBit("Forbid_Switch", Long.parseLong(this.GAME_ID));
    }

    /***
     * 禁止看广告的开始时间
     */
    public String Forbid_Begin_Time() {
        return rur.get("Forbid_Begin_Time");
    }

    /***
     * 禁止看广告的结束时间
     */
    public String Forbid_End_Time() {
        return rur.get("Forbid_End_Time");
    }

    /***
     * 设备系统低于以下禁止登录
     */
    public int getMin_System_Version() {
        return Integer.parseInt(rur.get("Min_System_Version"));
    }

    /***
     * 请求广告与收下奖励时间间隔
     */
    public int ADV_Interval() {
        return Integer.parseInt(rur.get("ADV_Interval"));
    }

    public String Notification() {
        return rur.get("Notification");
    }

    /***
     * 用户奖励比例
     */
    public double ADV_Percent() {
        return Double.parseDouble(rur.get("ADV_Percent"));
    }

    /***
     * 用户奖励上限
     */
    public int Reward_Limit() {
        return Integer.parseInt(rur.get("Reward_Limit"));
    }

    public boolean isGather_Choice() {
        return rur.getBit("Gather_Choice", Long.parseLong(this.GAME_ID));
    }

    public int Gather_Big_Limit() {
        return Integer.parseInt(rur.get("Gather_Big_Limit"));
    }

    public int Gather_Small_Limit() {
        return Integer.parseInt(rur.get("Gather_Small_Limit"));
    }

    /***
     * 同型号手机限制设备数量
     *
     */
    public int Same_Model_Limit() {
        return Integer.parseInt(rur.get("Same_Mac_Limit"));
    }

    /***
     * 同mac地址限制设备数量
     *
     */
    public int Same_Mac_Limit() {
        return Integer.parseInt(rur.get("Same_Model_Limit"));
    }

    /***
     * 是否开启截图设置
     *
     */
    public boolean isShot_Switch() {
        return rur.getBit("Shot_Switch", Long.parseLong(this.GAME_ID));
    }

    /***
     * 代码位ecpm值大于....
     *
     */
    public double Codebit_Max_val() {
        return Double.parseDouble(rur.get("Codebit_Max_val"));
    }

    /***
     * 当日允许用户提交截图订单上限
     *
     */
    public int Daily_Max_Submit_Num() {
        return Integer.parseInt(rur.get("Daily_Max_Submit_Num"));
    }

    /***
     * 领取奖励观看广告次数
     *
     */
    public int Daily_Max_Watch_Num() {
        return Integer.parseInt(rur.get("Daily_Max_Watch_Num"));
    }

    /***
     * 用户免审核提现金额
     *
     */
    public String Withdraw_Nojudge_Money() {
        return rur.get("Withdraw_Nojudge_Money");
    }

    /***
     * 提现比例
     */
    public String Withdraw_Percentage() {
        return rur.get("Withdraw_Percentage");
    }

    /***
     * 每日提现次数 功能开关
     *  true | false
     */
    public boolean isDaily_Withdraw_Switch() {
        return rur.getBit("Daily_Withdraw_Switch", Long.parseLong(this.GAME_ID));
    }

    /***
     * 每日提现次数
     *
     */
    public int Daily_Withdraw_Count() {
        return Integer.parseInt(rur.get("Daily_Withdraw_Count"));
    }

}
