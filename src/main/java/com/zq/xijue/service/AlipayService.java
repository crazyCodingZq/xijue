package com.zq.xijue.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.zq.xijue.core.ResultVO;
import com.zq.xijue.core.alipay.AlipayConfig;
import com.zq.xijue.entity.SysOrder;
import com.zq.xijue.entity.SysUser;
import com.zq.xijue.mapper.SysOrderMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Author: zhangQ
 * @Date: 2020/5/6 21:37
 */
@Service
public class AlipayService {
    private final Logger log = LoggerFactory.getLogger(AlipayService.class);

    @Autowired
    private AlipayConfig alipayConfig;
    @Autowired
    private AlipayClient alipayClient;
    @Autowired
    private SysOrderMapper sysOrderMapper;

    @Autowired
    private SysUserService sysUserService;

    /**
     * @param orderNo: 订单编号
     * @param amount:  实际支付金额
     * @param body:    订单描述
     * @return
     * @Description: 创建支付宝订单
     * @Author:
     * @Date: 2020/5/6
     */
    public void createOrder(String orderNo, double amount, String body, HttpServletResponse httpResponse) throws AlipayApiException, IOException {
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradePagePayModel model = new AlipayTradePagePayModel();
        model.setSubject(body);
        model.setOutTradeNo(orderNo);
        model.setTotalAmount(String.valueOf(amount));
        model.setProductCode("FAST_INSTANT_TRADE_PAY"); //FAST_INSTANT_TRADE_PAY //QUICK_MSECURITY_PAY
        //.setPassbackParams("公用回传参数，如果请求时传递了该参数，则返回给商户时会回传该参数");

        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.page.pay
        AlipayTradePagePayRequest ali_request = new AlipayTradePagePayRequest();
        ali_request.setBizModel(model);
        ali_request.setNotifyUrl(alipayConfig.getNotifyUrl());// 回调地址
        ali_request.setReturnUrl(alipayConfig.getReturnUrl());// 回调地址
        AlipayTradePagePayResponse response = alipayClient.pageExecute(ali_request);
        String form = response.getBody();
        httpResponse.setContentType("text/html;charset=UTF-8");
        httpResponse.getWriter().write(form); //直接将完整的表单html输出到页面
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }

    /**
     * @param tradeStatus: 支付宝交易状态
     * @param orderNo:     订单编号
     * @param tradeNo:     支付宝订单号
     * @return
     * @Description:
     * @Author:
     * @Date: 2020/5/6
     */
    public boolean notify(String tradeStatus, String orderNo, String tradeNo) {
        try {
            log.info("tradeStatus={}", tradeStatus);
            if ("TRADE_FINISHED".equals(tradeStatus)
                    || "TRADE_SUCCESS".equals(tradeStatus)) {
                // 支付成功，根据业务逻辑修改相应数据的状态
                SysOrder sysOrder = sysOrderMapper.queryByOrderCode(orderNo);
                SysUser sysUser = sysUserService.getByMobile(sysOrder.getUid());
                LocalDateTime now = LocalDateTime.now();
                if (sysOrder.getOrderType() == 0) {
                    //季卡
                    now.plusMonths(3);
                    sysUser.setVipLevel(1);
                } else if (sysOrder.getOrderType() == 1) {
                    //年卡
                    now.plusYears(1);
                    sysUser.setVipLevel(1);
                } else if (sysOrder.getOrderType() == 2) {
                    //终身会员卡
                    sysUser.setVipLevel(20);
                }
                Date date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
                sysUser.setVipLimit(date);
                log.info("sysuser={}", sysUser.toString());
                sysUserService.updateUser(sysUser);
                sysOrderMapper.updatePaymentState(orderNo, tradeNo);
                return true;
            }
        } catch (Exception e) {
            log.error("update order status error", e);
            return false;
        }
        return false;
    }

    /**
     * @param request
     * @return
     * @Description: 校验签名
     * @Author:
     * @Date: 2020/5/6
     */
    public boolean rsaCheckV1(HttpServletRequest request) {
        try {
            //获取支付宝GET过来反馈信息
            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
                String name = (String) iter.next();
                String[] values = requestParams.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
                }
                params.put(name, valueStr);
            }

            boolean verifyResult = AlipaySignature.rsaCheckV1(params, alipayConfig.getAlipayPublicKey(), alipayConfig.getCharset(), alipayConfig.getSignType());
            return verifyResult;
        } catch (AlipayApiException e) {
            log.debug("verify sigin error, exception is:{}", e);
            return false;
        }
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
    public ResultVO refund(String orderNo, double amount, String refundReason) {
        if (StringUtils.isBlank(orderNo)) {
            return new ResultVO(false, "订单编号不能为空");
        }
        if (amount <= 0) {
            return new ResultVO(false, "退款金额必须大于0");
        }

        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        // 商户订单号
        model.setOutTradeNo(orderNo);
        // 退款金额
        model.setRefundAmount(String.valueOf(amount));
        // 退款原因
        model.setRefundReason(refundReason);
        // 退款订单号(同一个订单可以分多次部分退款，当分多次时必传)
        // model.setOutRequestNo(UUID.randomUUID().toString());
        AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();
        alipayRequest.setBizModel(model);
        AlipayTradeRefundResponse alipayResponse = null;
        try {
            alipayResponse = alipayClient.execute(alipayRequest);
        } catch (AlipayApiException e) {
            log.error("订单退款失败，异常原因:{}", e);
        }
        if (alipayResponse != null) {
            String code = alipayResponse.getCode();
            String subCode = alipayResponse.getSubCode();
            String subMsg = alipayResponse.getSubMsg();
            if ("10000".equals(code)
                    && StringUtils.isBlank(subCode)
                    && StringUtils.isBlank(subMsg)) {
                // 表示退款申请接受成功，结果通过退款查询接口查询
                // 修改用户订单状态为退款
                return new ResultVO(true, "订单退款成功");
            }
            return new ResultVO(false, subCode + ":" + subMsg);
        }
        return new ResultVO(false, "订单退款失败");
    }
}
