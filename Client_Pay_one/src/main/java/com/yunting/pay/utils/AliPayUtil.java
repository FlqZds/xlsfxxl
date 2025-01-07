package com.yunting.pay.utils;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayFundTransUniTransferModel;
import com.alipay.api.domain.Participant;
import com.alipay.api.request.AlipayFundTransUniTransferRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipayFundTransUniTransferResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.yunting.common.exception.AppException;
import com.yunting.common.results.ResponseEnum;
import com.yunting.common.results.ResultMessage;
import com.yunting.common.utils.FS;
import com.yunting.common.utils.SpringRollBackUtil;
import com.yunting.pay.config.AliPayConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Objects;

@Slf4j
@Component("AliPayUtil")
public class AliPayUtil {
    public static final String IdentityType = "ALIPAY_LOGON_ID";
    public static final String BizScene = "DIRECT_TRANSFER";
    public static final String ProductCode = "TRANS_ACCOUNT_NO_PWD";
    @Resource(name = "AliPayConfig")
    private AliPayConfig aliPayConfig;

    /***
     * 初始化支付宝客户端
     * @return AlipayClient 支付宝客户端
     */
    public AlipayClient initAlipayClient() {
        // 初始化SDK
        AlipayClient alipayClient = null;
        try {
            AlipayConfig alipayConfig = aliPayConfig.getAlipayConfig();
            if (Objects.nonNull(alipayConfig)) {
                alipayClient = new DefaultAlipayClient(alipayConfig);
                return alipayClient;
            } else {
                log.info("支付宝配置文件加载失败");
                throw new AppException(ResponseEnum.ALIPAY_CLIENT_INIT_FAILED);
            }
        } catch (AlipayApiException e) {
            log.info("支付宝客户端初始化失败,错误信息:" + e);
            SpringRollBackUtil.rollBack();
            throw new AppException(ResponseEnum.ALIPAY_CLIENT_INIT_FAILED);
        }
    }

    /***
     * 获取用户基本信息和Uid
     *
     */
    public void uID() {
        AlipayClient alipayClient = initAlipayClient();

        AlipayUserInfoShareRequest request = new AlipayUserInfoShareRequest();
        try {
            AlipayUserInfoShareResponse response = alipayClient.execute(request, "accessToken");
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
//        if (response.isSuccess()) {
//            System.out.println("调用成功");
//        } else {
//            System.out.println("调用失败");
//        }
    }


    /***
     * 支付宝单笔转账接口调用
     * @param OutBizNo 商户转账唯一订单号
     * @param transAmount 该笔订单总金额  单位为元,精确到小数点后两位
     * @param OrderTitle 转账业务的标题
     * @param Remark 转账业务的业务备注
     * @param payeeInfo 收款方信息
     *
     *                       * @param bizScene 业务场景。单笔无密转账固定为 DIRECT_TRANSFER
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultMessage pay(String OutBizNo, BigDecimal transAmount, String OrderTitle, String Remark, Participant payeeInfo) {
        AlipayClient alipayClient = this.initAlipayClient();

        // 构造请求参数以调用接口
        AlipayFundTransUniTransferRequest request = new AlipayFundTransUniTransferRequest();
        AlipayFundTransUniTransferModel model = new AlipayFundTransUniTransferModel();

        // 设置商家侧唯一订单号
        model.setOutBizNo(OutBizNo);

        // 设置订单总金额
        model.setTransAmount(transAmount.toPlainString());

        // 设置描述特定的业务场景
        model.setBizScene(BizScene);

        // 设置业务产品码
        model.setProductCode(ProductCode);

        // 设置转账业务的标题
        model.setOrderTitle(OrderTitle);


        model.setPayeeInfo(payeeInfo);

        // 设置业务备注
        model.setRemark(Remark);

        // 设置转账业务请求的扩展参数
        model.setBusinessParams("{\"payer_show_name_use_alias\":\"true\"}");

        request.setBizModel(model);
        AlipayFundTransUniTransferResponse response = null;
        try {
            response = alipayClient.certificateExecute(request);
        } catch (AlipayApiException e) {
            log.error("支付宝转账接口调用失败,错误详情:" + e);
            SpringRollBackUtil.rollBack();
            throw new AppException(ResponseEnum.ALIPAY_TRANSFER_INVOKE_FAILED);
        }

        if (response.isSuccess()) {
            log.info("调用成功");

            log.info("商户订单号:" + response.getOutBizNo());
            log.info("支付宝转账订单号:" + response.getOrderId());
            log.info("支付宝支付资金流水号:" + response.getPayFundOrderId());
            log.info("订单支付时间:" + response.getTransDate());
            log.info("转账单据状态:" + response.getStatus());
            log.info("支付码:" + response.getSubCode());
            log.info("支付信息:" + response.getSubMsg());
            return new ResultMessage(ResponseEnum.ALIPAY_TRANSFER_SUCCESSFUL, null);
        } else {
            log.error("调用失败");
            // sdk版本是"4.38.0.ALL"及以上,可以参考下面的示例获取诊断链接
//            String diagnosisUrl = DiagnosisUtils.getDiagnosisUrl(response);
            log.error("商户订单号:" + response.getOutBizNo());
            log.error("支付宝转账订单号:" + response.getOrderId());
            log.error("支付宝支付资金流水号:" + response.getPayFundOrderId());
            log.error("订单支付时间:" + response.getTransDate());
            log.error("转账单据状态:" + response.getStatus());

            log.info("支付码:" + response.getSubCode());
            log.info("支付信息:" + response.getSubMsg());
            return new ResultMessage(response.getCode(), response.getSubMsg(), null);
        }
    }
}
