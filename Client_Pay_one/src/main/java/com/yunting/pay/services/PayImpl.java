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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultMessage applyWithdraw(Long playerId, String payId, String realName) {
        ResultMessage resultMessage = new ResultMessage(ResponseEnum.SUCCESS, null);
        int see_count;
        String is_see_enc = rur.get(playerId + "withdraw") + "";
        see_count = Integer.parseInt(is_see_enc);
        if (see_count != 88) {
            log.info("玩家提现之前要先去看一个激励广告", new AppException(ResponseEnum.PLAYER_NO_SEE_ENCOURAGE));
            rur.setEx("withdraw" + playerId, "0", 5, TimeUnit.MINUTES);
            return new ResultMessage(ResponseEnum.PLAYER_NO_SEE_ENCOURAGE, null);
        }

        BigDecimal playerRed_Need_reduce = BigDecimal.ZERO;
        Player player = playerMapper.selectPlayerByPlayerId(playerId);
        Long pName = null;
        Long pID = null;

        String bindPayLoginId = player.getPayLoginId();//玩家已绑定的支付宝id
        String bindRealName = player.getRealName();//玩家已绑定的姓名

        pID = playerMapper.selectPlayerByPayID(payId);//查询数据库中是否有人绑定该pay信息
        pName = playerMapper.selectPlayerByRealName(realName);//查询数据库中是否有人绑定该pay信息

        if (bindPayLoginId != null && bindRealName != null) {
            if (playerId.equals(player.getPlayerId()) == false) {//但是玩家id和传过来的不是同一个人
                if (payId.equals(bindPayLoginId) && realName.equals(bindRealName)) {//绑定信息相同
                    log.warn("该玩家已经被绑定,请勿重复绑定", new AppException(ResponseEnum.PLAYER_ALIPAY_ALREADY_BIND));
                    throw new AppException(ResponseEnum.PLAYER_ALIPAY_ALREADY_BIND);
                }
            } else { //是同一个人 ,但是这次传的和数据库中的不一样
                if (payId.equals(bindPayLoginId) == false || realName.equals(bindRealName) == false) {//绑定信息相同
                    log.warn("您所提交的支付宝账号信息与上次有所不同", new AppException(ResponseEnum.ALIPAY_INFO_DIFFERENCE));
                    throw new AppException(ResponseEnum.ALIPAY_INFO_DIFFERENCE);
                }
            }
        }


        BigDecimal playerInRed = player.getInRed(); //用户当前余额

        DayBehaveRecordlist dayRecord = dayBehaveMapper.getDayLastDayBehaveRecordlistByPlayerId(playerId);
        BigDecimal dayCash = dayRecord.getDayCash(); //用户当日已提现总金额

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

        if (l >= withdrawCount) {
            log.warn("玩家当日提现次数已超过上限，无法提现", new AppException(ResponseEnum.PLAYER_WITHDRAW_COUNT_OVER_LIMIT));
            throw new AppException(ResponseEnum.PLAYER_WITHDRAW_COUNT_OVER_LIMIT);
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

        Integer i = transAmount.compareTo(withdrawNojudgeMoney); //-1 0 1  <=>   是否触发审核
        String limitRebackMoney_str = withdrawMapper.getLimitRebackMoney();
        BigDecimal limitRebackMoney = new BigDecimal(limitRebackMoney_str);//最低返现金额
        Integer j = transAmount.compareTo(limitRebackMoney); //-1 0 1  <=>   是否触发返现

        //实际返现金额
        BigDecimal rebackVal = null;
        if (j != -1) {

            BigDecimal thisRebackPer_end = null;
            BigDecimal thisRebackPer_begin = null;

            if (withdrawMapper.getThisReback(dayCash.add(transAmount)) == null) {
                thisRebackPer_begin = BigDecimal.ZERO;
            } else {
                thisRebackPer_begin = new BigDecimal(withdrawMapper.getThisReback(dayCash.add(transAmount)));//begin返现比例
            }
            if (withdrawMapper.getThisReback(dayCash) == null) {
                thisRebackPer_end = BigDecimal.ZERO;
            } else {
                thisRebackPer_end = new BigDecimal(withdrawMapper.getThisReback(dayCash));//end (已提现全部金额的返现比例)
            }

            thisRebackPer_begin = thisRebackPer_begin.multiply(Percentage);
            thisRebackPer_end = thisRebackPer_end.multiply(Percentage);

            BigDecimal toReduce = dayCash.multiply(thisRebackPer_end);  //计算公式后面要减掉的那一坨
            toReduce = toReduce.setScale(2, BigDecimal.ROUND_DOWN);

            BigDecimal added = dayCash.add(transAmount); //总的+这次的
            added = added.multiply(thisRebackPer_begin);               //计算公式前面那一坨
            added = added.setScale(2, BigDecimal.ROUND_DOWN);


            rebackVal = added.subtract(toReduce);//返现金额
            log.info("提现金额:" + transAmount + "金额触发返现,返现金额为:" + rebackVal
                    + "  该次前面触发的返现比例为:" + thisRebackPer_begin + "该次后面触发的返现比例为:" + thisRebackPer_end);
        } else {
            rebackVal = new BigDecimal("0");
            log.info("提现金额:" + transAmount + "金额未触发返现,最低为:" + limitRebackMoney + "触发");
        }

        WithdrawRecord withdrawRecord = WithdrawRecord.builder()
                .withdrawMoney(transAmount).returnMoney(rebackVal)
                .playerId(playerId).packageName(st.PackageName())
                .withdrawPercentageNow(String.valueOf(withdrawPercentage))
                .withdrawTime(LocalDateTime.now()).wxNickname(player.getWxNickname()).build();

        try {

            if (i != -1) {
                //审核
                if (player.getRealName() == null || player.getPayLoginId() == null) {  //提现成功,绑定该用户支付宝
                    rs.hPutIfAbsent("payInfo", playerId + "payId", payId);
                    rs.hPutIfAbsent("payInfo", playerId + "realName", realName);
                }

                withdrawRecord.setWithdrawStatus('1');
                resultMessage.setMessage(ResponseEnum.WITHDRAW_ORDER_MENTIONED.getMessage());
                resultMessage.setCode(ResponseEnum.WITHDRAW_ORDER_MENTIONED.getCode());
            } else {
                //免审核的提现
                String OutBizNo = Pay_Order_Header + UUID.randomUUID().toString().replace("-", "");//订单流水号
                String title = "提现成功! 乐益消消乐祝您龙年行大运,完事皆如意";
                String remark = "";
                Participant alipayaccount = new Participant();
                alipayaccount.setIdentityType(IdentityType);
                alipayaccount.setIdentity(payId);
                alipayaccount.setName(realName);
                transAmount = transAmount.add(rebackVal); //返现 提现金额一起发
                BigDecimal cash = BigDecimal.ZERO;
                cash = transAmount.setScale(2, BigDecimal.ROUND_DOWN);

                withdrawRecord.setWithdrawStatus('0');      //该订单为免审核-通过
                playerRed_Need_reduce = (transAmount.subtract(rebackVal));
                dayBehaveMapper.changeDayBehaveRecordWithdrawCash(dayRecord.getDayId(), playerRed_Need_reduce);  //当日提现金额累加

                aliPayUtil.pay(OutBizNo, cash, title, remark, alipayaccount);

                if (bindRealName == null || bindPayLoginId == null) {  //提现成功,绑定该用户支付宝
                    playerMapper.refreshPlayerRealInfo(playerId, payId, realName);
                    log.info("玩家免审核提现成功,绑定该用户支付宝");
                }

                resultMessage.setMessage(ResponseEnum.NO_JUDGE_ORDER_SUCCESSFUL.getMessage());
                resultMessage.setCode(ResponseEnum.NO_JUDGE_ORDER_SUCCESSFUL.getCode());
            }
            withdrawMapper.insertWithdrawRecord(withdrawRecord);              //插入提现记录

            rur.delete(playerId + "withdraw");
            playerMapper.updatePlayerInRed(playerId, playerRed_Need_reduce.multiply(withdrawPercentage).negate());  //玩家余额减少
            rs.hIncrBy("withdrawCount", playerId + "", 1);           //玩家当日提现次数+1
            BigDecimal redWithDrew = playerMapper.selectInRedByPlayerId(playerId, player.getGameId());  //提现后余额
            resultMessage.setData(redWithDrew);
        } catch (AppException e) {
            SpringRollBackUtil.rollBack();
            log.error("玩家免审核提现失败,错误信息:" + e, new AppException(ResponseEnum.PLAYER_WITHDRAW_FAILED));
            resultMessage.setMessage(e.getMessage());
            resultMessage.setCode(e.getCode());
            return resultMessage;
        }
        return resultMessage;
    }

    /***
     * 绑定支付宝
     * playerId 认证的玩家id
     * payId 支付宝用户id
     * realName 姓名
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer manIdentify(Long playerId, String payId, String realName) {
        Long pName = playerMapper.selectPlayerByPayID(payId);
        Long pID = playerMapper.selectPlayerByRealName(realName);
        if (pName != null || pID != null) {
            log.warn("该支付宝账号已被其他玩家绑定", new AppException(ResponseEnum.PLAYER_ALIPAY_ALREADY_BIND));
            throw new AppException(ResponseEnum.PLAYER_ALIPAY_ALREADY_BIND);
        }
        int i = playerMapper.refreshPlayerRealInfo(playerId, payId, realName);
        return i;
    }
}
