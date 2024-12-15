package com.yunting.client.common.config;

import com.alipay.api.AlipayConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("AliPayConfig")
public class AliPayConfig {

    /***
     * 支付宝详细信息
     */
    @Value("${pay_appID}")
    public String appId;
    @Value("${pay_AppCertPath}")
    public String AppCertPath;  //应用公钥证书文件路径
    @Value("${pay_AlipayPublicCertPath}")
    public String AlipayPublicCertPath;             //支付宝公钥证书文件路径
    @Value("${pay_RootCertPath}")
    public String RootCertPath;  //支付宝根证书文件路径

    @Value("${pay_serverUrl}")
    public String serverUrl;
    @Value("${pay_privateKey}")
    public String privateKey;

    private AlipayConfig alipayConfig = new AlipayConfig();


    public AlipayConfig getAlipayConfig() {
        alipayConfig.setPrivateKey(privateKey);
        alipayConfig.setServerUrl(serverUrl);
        alipayConfig.setAppId(appId);
        alipayConfig.setCharset("UTF-8");
        alipayConfig.setSignType("RSA2");
        alipayConfig.setFormat("json");
        alipayConfig.setAppCertPath(AppCertPath);
        alipayConfig.setAlipayPublicCertPath(AlipayPublicCertPath);
        alipayConfig.setRootCertPath(RootCertPath);
        return this.alipayConfig;
    }
}
