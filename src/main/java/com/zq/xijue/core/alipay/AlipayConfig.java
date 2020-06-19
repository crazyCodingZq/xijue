package com.zq.xijue.core.alipay;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @Author: zhangQ
 * @Date: 2020/5/6 21:28
 */
@Data
@Component
@ConfigurationProperties(prefix = "pay.alipay")
public class AlipayConfig {
    /**
     * 最大查询次数
     */
    private static int maxQueryRetry = 5;
    /**
     * 查询间隔（毫秒）
     */
    private static long queryDuration = 5000;
    /**
     * 最大撤销次数
     */
    private static int maxCancelRetry = 3;
    /**
     * 撤销间隔（毫秒）
     */
    private static long cancelDuration = 3000;
    /**
     * 支付宝gatewayUrl
     */
    private String gatewayUrl;
    /**
     * 商户应用id
     */
    private String appid;
    /**
     * RSA私钥，用于对商户请求报文加签
     */
    private String appPrivateKey;
    /**
     * 支付宝RSA公钥，用于验签支付宝应答
     */
    private String alipayPublicKey;
    /**
     * 签名类型
     */
    private String signType = "RSA2";
    /**
     * 格式
     */
    private String formate = "json";
    /**
     * 编码
     */
    private String charset = "UTF-8";
    /**
     * 同步地址
     */
    private String returnUrl;
    /**
     * 异步地址
     */
    private String notifyUrl;

    @Bean
    public AlipayClient alipayClient() {
        return new DefaultAlipayClient(getGatewayUrl(),
                getAppid(),
                getAppPrivateKey(),
                getFormate(),
                getCharset(),
                getAlipayPublicKey(),
                getSignType());
    }
}
