package com.zq.xijue.service;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import com.zq.xijue.core.ResultVO;
import com.zq.xijue.core.alipay.WxPayAppConfig;
import com.zq.xijue.entity.SysOrder;
import com.zq.xijue.entity.SysUser;
import com.zq.xijue.util.IpUtils;
import com.zq.xijue.util.SnowflakeIdFactory;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zhangQ
 * @Date: 2020/5/6 23:05
 */
@Service
public class WxPayService {
    private final Logger logger = LoggerFactory.getLogger(WxPayService.class);
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysOrderService sysOrderService;

    @Autowired
    private WxPayAppConfig wxPayAppConfig;

    /**
     * @param orderNo: 订单编号
     * @param amount:  实际支付金额
     * @param body:    订单描述
     * @return
     * @Description: 微信支付统一下单
     * @Author:
     * @Date: 2019/8/1
     */
    public ResultVO unifiedOrder(String orderNo, double amount, String body, HttpServletRequest request) {
        Map<String, String> requestMap = new HashMap<>();
        try {
            WXPay wxpay = new WXPay(wxPayAppConfig);
            requestMap.put("nonce_str", RandomStringUtils.randomAlphanumeric(10));
            requestMap.put("body", body);                                     // 商品描述
            requestMap.put("out_trade_no", orderNo);                          // 商户订单号
            requestMap.put("total_fee", String.valueOf((int) (amount * 100)));   // 总金额,单位分
            requestMap.put("spbill_create_ip", IpUtils.getIpAddr(request)); // 终端IP
            requestMap.put("notify_url", wxPayAppConfig.getPayNotifyUrl());   // 接收微信支付异步通知回调地址
            requestMap.put("trade_type", "NATIVE");                              // JSAPI -JSAPI支付 NATIVE -Native支付 APP -APP支付
            Map<String, String> responseMap = wxpay.unifiedOrder(requestMap);
            //获取返回码
            String returnCode = responseMap.get("return_code");
            String returnMsg = responseMap.get("return_msg");
            String errCodeDes = "";
            //若返回码为SUCCESS，则会返回一个result_code,再对该result_code进行判断
            if ("SUCCESS".equals(returnCode)) {
                String resultCode = responseMap.get("result_code");
                errCodeDes = responseMap.get("err_code_des");
                if ("SUCCESS".equals(resultCode)) {
                    // 返回二维码链接
                    return new ResultVO(responseMap.get("code_url"));
                }
            }
            logger.error("wepay unifiedOrder error,return_msg={}", returnMsg);
            logger.error("wepay unifiedOrder error,errCodeDes={}", errCodeDes);
            return new ResultVO(false, "系统错误");
//            if (responseMap == null || responseMap.isEmpty()) {
//                return new ResultVO(false, "获取预支付交易会话标识失败");
//            }
//            // 3、签名生成算法
//            Long time = System.currentTimeMillis() / 1000;
//            String timestamp = time.toString();
//            returnMap.put("appid", wxPayAppConfig.getAppID());
//            returnMap.put("partnerid", wxPayAppConfig.getMchID());
//            returnMap.put("prepayid", responseMap.get("prepay_id"));
//            returnMap.put("noncestr", responseMap.get("nonce_str"));
//            returnMap.put("timestamp", timestamp);
//            returnMap.put("package", "Sign=WXPay");
//            returnMap.put("sign", WXPayUtil.generateSignature(returnMap, wxPayAppConfig.getKey()));//微信支付签名
//            return new ResultVO(requestMap);
        } catch (Exception e) {
            logger.error("订单号：{}，错误信息：{}", orderNo, e.getMessage(), e);
            return new ResultVO(false, "微信支付统一下单失败");
        }
    }

    /**
     * @param notifyStr: 微信异步通知消息字符串
     * @return
     * @Description: 订单支付异步通知
     * @Author:
     * @Date: 2019/8/1
     */
    public String notify(String notifyStr) throws Exception {
        String xmlBack = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[报文为空]]></return_msg></xml> ";
        // 转换成map
        Map<String, String> resultMap = WXPayUtil.xmlToMap(notifyStr);
        StringBuffer sb = new StringBuffer();
        for (String key : resultMap.keySet()) {
            sb.append("{key=").append(key).append(";;;;value=").append(resultMap.get(key)).append("}====");
        }
        logger.info("notify map={}", sb.toString());
        WXPay wxpayApp = new WXPay(wxPayAppConfig);
        if (wxpayApp.isResponseSignatureValid(resultMap)) {
            String returnCode = resultMap.get("return_code");  //状态
            String outTradeNo = resultMap.get("out_trade_no");//商户订单号
            logger.info("wepay notify method success；orderNo={}", outTradeNo);
            String transactionId = resultMap.get("transaction_id");
            if ("SUCCESS".equals(returnCode)) {
                if (StringUtils.isNotBlank(outTradeNo)) {
                    //微信返回订单支付成功，更新用户vip等级、vip过期时间。同时更新订单状态
                    updateUserAndOrder(outTradeNo, transactionId);
                    logger.info("微信手机支付回调成功,订单号:{}", outTradeNo);
                    xmlBack = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
                }
            }
        }
        return xmlBack;
    }

    public ResultVO queryWePayOrder(String orderNo) throws Exception {
        Map<String, String> requestMap = new HashMap<>();
        WXPay wxpay = new WXPay(wxPayAppConfig);
        requestMap.put("out_trade_no", orderNo); // 商户订单号
        logger.info("queryWePayOrder oderNo={}", orderNo);
        Map<String, String> responseMap = wxpay.orderQuery(requestMap);
        //获取返回码
        String returnCode = responseMap.get("return_code");
        String returnMsg = responseMap.get("return_msg");
        String transactionId = responseMap.get("transaction_id");//微信支付订单号
        String outTradeNo = responseMap.get("out_trade_no");//商户订单号
        String errCodeDes = "";
        String tradeStateDesc = "";
        //若返回码为SUCCESS，则会返回一个result_code,再对该result_code进行判断
        if ("SUCCESS".equals(returnCode)) {
            String resultCode = responseMap.get("result_code");
            errCodeDes = responseMap.get("err_code_des");
            if ("SUCCESS".equals(resultCode)) {
                String tradeState = responseMap.get("trade_state");
                tradeStateDesc = responseMap.get("trade_state_desc");
                if ("SUCCESS".equals(tradeState)) {
                    //微信返回订单支付成功，更新用户vip等级、vip过期时间。同时更新订单状态
                    updateUserAndOrder(outTradeNo, transactionId);
                    return new ResultVO(true, "支付成功");
                }
            }
        }
        if (!StringUtils.isEmpty(returnMsg)) {
            logger.error("wepay unifiedOrder error,return_msg={}", returnMsg);
        }
        if (!StringUtils.isEmpty(errCodeDes)) {
            logger.error("wepay unifiedOrder error,errCodeDes={}", errCodeDes);
        }
        if (!StringUtils.isEmpty(tradeStateDesc)) {
            logger.error("wepay unifiedOrder error,tradeStateDesc={}", tradeStateDesc);
        }
        return new ResultVO(false, "系统错误");
    }

    /**
     * 订单支付成功，更新用户vip等级和vip过期时间，并更新订单状态为已支付
     */
    private void updateUserAndOrder(String outTradeNo, String transactionId) {
        // 更新订单状态
        SysOrder sysOrder = sysOrderService.queryOrderDetail(outTradeNo);
        SysUser sysUser = sysUserService.getByMobile(sysOrder.getUid());
        LocalDateTime now = LocalDateTime.now();
        if (sysOrder.getOrderType() == 0) {
            //季卡
            now = now.plusMonths(3);
            sysUser.setVipLevel(1);
        } else if (sysOrder.getOrderType() == 1) {
            //年卡
            now = now.plusYears(1);
            sysUser.setVipLevel(1);
        } else if (sysOrder.getOrderType() == 2) {
            //终身会员卡
            sysUser.setVipLevel(20);
        }
        Date date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        sysUser.setVipLimit(date);
        sysUserService.updateUser(sysUser);
        sysOrderService.updatePaymentState(outTradeNo, transactionId);
    }

    /**
     * @param orderNo:      订单编号
     * @param amount:       实际支付金额
     * @param refundReason: 退款原因
     * @return
     * @Description: 退款
     * @Author: XCK
     * @Date: 2019/8/6
     */
    public ResultVO refund(String orderNo, double amount, String refundReason) throws Exception {
        if (StringUtils.isBlank(orderNo)) {
            return new ResultVO(false, "订单编号不能为空");
        }
        if (amount <= 0) {
            return new ResultVO(false, "退款金额必须大于0");
        }

        Map<String, String> responseMap = new HashMap<>();
        Map<String, String> requestMap = new HashMap<>();
        WXPay wxpay = new WXPay(wxPayAppConfig);
        requestMap.put("out_trade_no", orderNo);
        SnowflakeIdFactory idWorker = new SnowflakeIdFactory(1, 2);
        requestMap.put("out_refund_no", String.valueOf(idWorker.nextId()));
        requestMap.put("total_fee", "订单支付时的总金额，需要从数据库查");
        requestMap.put("refund_fee", String.valueOf((int) (amount * 100)));//所需退款金额
        requestMap.put("refund_desc", refundReason);
        try {
            responseMap = wxpay.refund(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String return_code = responseMap.get("return_code");   //返回状态码
        String return_msg = responseMap.get("return_msg");     //返回信息
        if ("SUCCESS".equals(return_code)) {
            String result_code = responseMap.get("result_code");       //业务结果
            String err_code_des = responseMap.get("err_code_des");     //错误代码描述
            if ("SUCCESS".equals(result_code)) {
                //表示退款申请接受成功，结果通过退款查询接口查询
                //修改用户订单状态为退款申请中或已退款。退款异步通知根据需求，可选
                //
                return new ResultVO("退款申请成功");
            } else {
                logger.info("订单号:{}错误信息:{}", orderNo, err_code_des);
                return new ResultVO(false, err_code_des);
            }
        } else {
            logger.info("订单号:{}错误信息:{}", orderNo, return_msg);
            return new ResultVO(false, return_msg);
        }
    }
}
