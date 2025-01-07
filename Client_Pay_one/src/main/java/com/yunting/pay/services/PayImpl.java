package com.yunting.pay.services;

import com.alipay.api.domain.Participant;
import com.yunting.common.exception.AppException;
import com.yunting.common.results.ResponseEnum;
import com.yunting.common.results.ResultMessage;
import com.yunting.common.utils.RedisUtil_Record;
import com.yunting.common.utils.RedisUtils_Wlan;
import com.yunting.common.utils.ST;
import com.yunting.common.utils.SpringRollBackUtil;
import com.yunting.pay.entity.DayBehaveRecordlist;
import com.yunting.pay.entity.Player;
import com.yunting.pay.entity.WithdrawRecord;
import com.yunting.pay.mapper.DayBehaveRecordlistMapper;
import com.yunting.pay.mapper.PlayerMapper;
import com.yunting.pay.mapper.WithdrawRecordMapper;
import com.yunting.pay.utils.AliPayUtil;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.yunting.common.results.ResponseEnum.PLAYER_WITHDRAW_FAILED;
import static com.yunting.common.utils.FS.*;
import static com.yunting.pay.utils.AliPayUtil.IdentityType;

@Service("PayServices")
@Slf4j
public class PayImpl implements PayServices {


    @Resource(name = "RedisUtil_Record")
    private RedisUtil_Record rur;

    @Resource(name = "RedisUtils")
    private RedisUtils_Wlan rs;

    @Resource(name = "PlayerMapper")
    private PlayerMapper playerMapper;

    @Resource(name = "WithdrawRecordMapper")
    private WithdrawRecordMapper withdrawMapper;

    @Resource(name = "DayBehaveRecordlistMapper")
    private DayBehaveRecordlistMapper dayBehaveMapper;

    @Resource(name = "AliPayUtil")
    AliPayUtil aliPayUtil;

    @Resource(name = "ST")
    private ST st;


    /***
     * 免审核提现申请接口
     * <p>
     * 提现订单生成
     * @param playerId  提现的玩家id
     * @param payId  提现的玩家支付宝id
     * @param realName 体现玩家的姓名
     * @param from  来源 [r=红包提现 o=订单提现]
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultMessage applyWithdraw(Long playerId, String payId, String realName, String from) {
        ResultMessage resultMessage = new ResultMessage(ResponseEnum.SUCCESS, null);

        String is_see_enc = rur.get(playerId + "withdraw") + "";
        if (is_see_enc == null) {
            log.info("玩家提现之前要先去看一个激励广告", new AppException(ResponseEnum.PLAYER_NO_SEE_ENCOURAGE));
            rur.set("withdraw" + playerId, "0");
            return new ResultMessage(ResponseEnum.PLAYER_NO_SEE_ENCOURAGE, null);
        }

        Long thisWithdraw_playerID = playerMapper.selectPlayerByPayInfo(payId, realName); //这个绑定了支付宝信息的玩家的id
        Player player = playerMapper.selectPlayerByPlayerId(playerId); //要提现的玩家


        String bindPayLoginId = player.getPayLoginId();//提现玩家已绑定的支付宝id
        String bindRealName = player.getRealName();//提现玩家已绑定的姓名

        if (thisWithdraw_playerID != null) {
            if (playerId.equals(thisWithdraw_playerID) == false) {//查到的玩家id和 传过来支付宝信息查到的用户的id不一致 (该支付宝信息已经被绑定了)
                log.warn("该玩家已经被绑定,请勿重复绑定", new AppException(ResponseEnum.PLAYER_ALIPAY_ALREADY_BIND));
                throw new AppException(ResponseEnum.PLAYER_ALIPAY_ALREADY_BIND);
            } else { //是同一个人 ,但是这次传的和数据库中的不一样
                if (bindPayLoginId != null && bindRealName != null) {
                    if (payId.equals(bindPayLoginId) == false || realName.equals(bindRealName) == false) {//绑定信息相同
                        log.warn("您所提交的支付宝账号信息与上次有所不同", new AppException(ResponseEnum.ALIPAY_INFO_DIFFERENCE));
                        throw new AppException(ResponseEnum.ALIPAY_INFO_DIFFERENCE);
                    }
                }
            }
        }

        BigDecimal playerInRed = player.getInRed(); //用户当前余额

        DayBehaveRecordlist dayRecord = dayBehaveMapper.getDayLastDayBehaveRecordlistByPlayerId(playerId);

        BigDecimal withdrawPercentage = new BigDecimal(st.Withdraw_Percentage());//提现比例
        Integer withdrawCount = st.Daily_Withdraw_Count();//获取该用户的 当日提现次数上限
        BigDecimal withdrawNojudgeMoney = new BigDecimal(st.Withdraw_Nojudge_Money()); //设置的免审核金额

        String playerTodayCount = "";
        if (rs.hExists("withdrawCount", playerId + "") == true) {
            playerTodayCount = rs.hGet("withdrawCount", playerId + "").toString();
        } else {
            rs.hPutIfAbsent("withdrawCount", playerId + "", "0");
            playerTodayCount = rs.hGet("withdrawCount", playerId + "").toString();
        }
        long l = Long.parseLong(playerTodayCount);

        if (st.isDaily_Withdraw_Switch() == true) {
            if (l >= withdrawCount) {
                log.warn("玩家当日提现次数已超过上限，无法提现", new AppException(ResponseEnum.PLAYER_WITHDRAW_COUNT_OVER_LIMIT));
                throw new AppException(ResponseEnum.PLAYER_WITHDRAW_COUNT_OVER_LIMIT);
            }
        }


        if (playerInRed.compareTo(Percentage) == -1) {
            log.warn("用户余额不足,无法提现", new AppException(ResponseEnum.PLAYER_NO_MORE_MONEY));
            throw new AppException(ResponseEnum.PLAYER_NO_MORE_MONEY);
        }

        //实际提现金额
        playerInRed = playerInRed.setScale(2, BigDecimal.ROUND_DOWN);  //省去10012的12
        BigDecimal transAmount = playerInRed.divide(withdrawPercentage);
        transAmount = transAmount.setScale(2, BigDecimal.ROUND_DOWN);
        log.info("要提现的值:" + transAmount);
        if (transAmount.compareTo(MAXRed) != -1) {
            log.error("提现金额超出200.00元,拒绝该次交易", new AppException(ResponseEnum.PLAYER_WITHDRAW_MONEY_TOO_LARGE));
            throw new AppException(ResponseEnum.PLAYER_WITHDRAW_MONEY_TOO_LARGE);
        }

        if (transAmount.compareTo(limitRed) == -1) {
            log.warn("提现金额太小，无法提现", new AppException(ResponseEnum.PLAYER_WITHDRAW_MONEY_TOO_SMALL));
            throw new AppException(ResponseEnum.PLAYER_WITHDRAW_MONEY_TOO_SMALL);
        }

        Integer i = transAmount.compareTo(withdrawNojudgeMoney); //-1 0 1  <=>   是否触发 该日总的免审核提现金额


        Integer j = transAmount.compareTo(limitRed); //-1 0 1  <=>   是否触发最低返现
        BigDecimal rebackVal = thisRedBackVal(transAmount, j); //该次提现的返现金额

        WithdrawRecord withdrawRecord = WithdrawRecord.builder()
                .withdrawMoney(transAmount)
                .returnMoney(rebackVal)
                .playerId(playerId)
                .packageName(st.PackageName())
                .withdrawFrom(from)
                .payLoginId(payId)
                .realName(realName)
                .withdrawPercentageNow(String.valueOf(withdrawPercentage))
                .withdrawTime(LocalDateTime.now()).wxNickname(player.getWxNickname()).build();

        if (player.getPayLoginId() == null || player.getRealName() == null) {
            //第一次提现的用户直接免密只提0.3
            withdrawRecord.setWithdrawMoney(limitRed);
            rebackVal = thisRedBackVal(limitRed, j);
            withdrawRecord.setReturnMoney(rebackVal);
            transAmount = limitRed;
            i = -1;
        }


        if (i != 1) {            //免审核的提现
            ResultMessage message = withdrawNojudge(playerId, payId, realName, player, dayRecord, withdrawPercentage, transAmount, rebackVal, withdrawRecord);//提现后的余额
            return message;
        } else {
            //审核
            withdrawRecord.setWithdrawStatus('1');
            rs.hIncrBy("withdrawCount", playerId + "", 1);           //玩家当日提现次数+1

            BigDecimal playerRed = transAmount.multiply(withdrawPercentage).negate();//玩家余额
            playerMapper.updatePlayerInRed(playerId, playerRed);  //玩家余额减少
            dayBehaveMapper.changeDayBehaveRecordWithdrawCash(dayRecord.getDayId(), transAmount, playerRed, playerRed, rebackVal, transAmount);  //当日提现金额,返现金额,当日提现打款金额累加
            withdrawMapper.insertWithdrawRecord(withdrawRecord);              //插入提现记录
            rur.delete(playerId + "withdraw");
            rs.hIncrBy("withdrawCount", playerId + "", 1);           //玩家当日提现次数+1
            BigDecimal redWithDrew = playerMapper.selectInRedByPlayerId(playerId, player.getGameId());  //提现后余额

            resultMessage.setData(redWithDrew);
            resultMessage.setMessage(ResponseEnum.WITHDRAW_ORDER_MENTIONED.getMessage());
            resultMessage.setCode(ResponseEnum.WITHDRAW_ORDER_MENTIONED.getCode());
            log.info("订单已提交审核");
            return resultMessage;
        }

    }

    /***
     * 玩家免审核的提现,同时绑定支付宝
     * @param playerId 玩家id
     * @param payId 支付宝账户
     * @param realName 姓名
     * @param player 玩家信息
     * @param dayRecord 玩家该日留存记录
     * @param withdrawPercentage 提现比例
     * @param transAmount 提现金额
     * @param rebackVal 返现金额
     * @param withdrawRecord 该次的提现记录
     * @return 免审核提现成功后的玩家余额
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultMessage withdrawNojudge(Long playerId, String payId, String realName, Player player, DayBehaveRecordlist dayRecord, BigDecimal withdrawPercentage, BigDecimal transAmount, BigDecimal rebackVal, WithdrawRecord withdrawRecord) {

        BigDecimal playerRed_Need_reduce;
        String OutBizNo = Pay_Order_Header + UUID.randomUUID().toString().replace("-", "");//订单流水号
        String title = "提现成功! 乐益消消乐祝您龙年行大运,万事皆如意";
        String remark = "";
        Participant alipayaccount = new Participant();
        alipayaccount.setIdentityType(IdentityType);
        alipayaccount.setIdentity(payId);
        alipayaccount.setName(realName);
        transAmount = transAmount.add(rebackVal); //返现 提现金额一起发
        BigDecimal cash = transAmount.setScale(2, BigDecimal.ROUND_DOWN);

        BigDecimal redWithDrew = BigDecimal.ZERO;  //提现后余额
        ResultMessage pay = aliPayUtil.pay(OutBizNo, cash, title, remark, alipayaccount);

        if (pay.getCode().equals("66666")) {
            withdrawRecord.setWithdrawStatus('0');      //该订单为免审核-通过
            playerRed_Need_reduce = (transAmount.subtract(rebackVal));

            //提现成功,绑定该用户支付宝
            playerMapper.refreshPlayerRealInfo(playerId, payId, realName);
            log.info("玩家免审核提现成功,绑定该用户支付宝");

            withdrawMapper.insertWithdrawRecord(withdrawRecord);              //插入提现记录

            BigDecimal playerRed = playerRed_Need_reduce.multiply(withdrawPercentage).negate();//玩家余额减少
            playerMapper.updatePlayerInRed(playerId, playerRed);
            //当日提现打款金额,返现打款金额累加
            dayBehaveMapper.changeDayBehaveRecordWithdrawCash(dayRecord.getDayId(), playerRed_Need_reduce, playerRed, playerRed, rebackVal, playerRed_Need_reduce);
            redWithDrew = playerMapper.selectInRedByPlayerId(playerId, player.getGameId());


            rur.delete(playerId + "withdraw");
            rs.hIncrBy("withdrawCount", playerId + "", 1);           //玩家当日提现次数+1
            return new ResultMessage(ResponseEnum.NO_JUDGE_ORDER_SUCCESSFUL, redWithDrew);
        } else {
            throw new AppException(pay.getCode(), pay.getMessage());
        }
    }

    /***
     * 判断触发返现,同时返回该次的返现金额,未触发则返回0
     * @param transAmount   该次提现的金额
     * @param j 是否触发返现
     * @return 返现金额, 未触发=0
     */
    private BigDecimal thisRedBackVal(BigDecimal transAmount, Integer j) {
        BigDecimal rebackVal;
        if (j != -1) {

            BigDecimal thisRebackPer = new BigDecimal(withdrawMapper.getThisReback(transAmount)).multiply(Percentage);

            rebackVal = transAmount.multiply(thisRebackPer).setScale(2, BigDecimal.ROUND_DOWN);
            log.info("提现金额:" + transAmount + "金额触发返现,返现金额为:" + rebackVal
                    + "  该次前面触发的返现比例为:" + thisRebackPer);
        } else {
            rebackVal = new BigDecimal("0");
            log.info("提现金额:" + transAmount + "金额未触发返现,最低为:" + limitRed + "触发");
        }
        return rebackVal;
    }

//    /***
//     * 绑定支付宝
//     * playerId 认证的玩家id
//     * payId 支付宝用户id
//     * realName 姓名
//     */
//    @Transactional(rollbackFor = Exception.class)
//    public Integer manIdentify(Long playerId, String payId, String realName) {
//        Long pName = playerMapper.selectPlayerByPayID(payId);
//        Long pID = playerMapper.selectPlayerByRealName(realName);
//        if (pName != null || pID != null) {
//            log.warn("该支付宝账号已被其他玩家绑定", new AppException(ResponseEnum.PLAYER_ALIPAY_ALREADY_BIND));
//            throw new AppException(ResponseEnum.PLAYER_ALIPAY_ALREADY_BIND);
//        }
//        int i = playerMapper.refreshPlayerRealInfo(playerId, payId, realName);
//        return i;
//    }
}
