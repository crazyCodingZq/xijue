package com.zq.xijue.controller;

import com.zq.xijue.core.ResultVO;
import com.zq.xijue.entity.SysOrder;
import com.zq.xijue.service.AlipayService;
import com.zq.xijue.service.SysOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 支付宝支付接口管理
 *
 * @Author: zhangQ
 * @Date: 2020/5/6 21:34
 */
@Controller
@RequestMapping("/alipay")
public class AlipayController {
    private final Logger logger = LoggerFactory.getLogger(AlipayController.class);
    @Autowired
    private AlipayService alipayService;
    @Autowired
    private SysOrderService sysOrderService;

    /**
     * 创建订单
     *
     * @param orderNo 订单号
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/createOrder", method = RequestMethod.GET)
    public void createOrder(String orderNo, HttpServletResponse httpResponse) {
        try {
            // 1、验证订单是否存在
            SysOrder order = sysOrderService.queryOrderDetail(orderNo);
            if (order == null) {
                return;
            }
            // 2、创建支付宝订单
            alipayService.createOrder(orderNo, order.getOrderPrice(), order.getOrderDesc(), httpResponse);
        } catch (Exception e) {
            logger.error("createOrder error:", e);
        }
    }

    /**
     * 支付宝支付同步回调通知
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/return")
    public String awaitReturn(HttpServletRequest request, HttpServletResponse response) {
        logger.info("alipay return method success");
        // 验证签名
        boolean flag = alipayService.rsaCheckV1(request);
        if (flag) {
            String tradeStatus = request.getParameter("trade_status"); // 交易状态
            String outTradeNo = request.getParameter("out_trade_no"); // 商户订单号
            logger.info("alipay notify method success;orderNo={}", outTradeNo);
            String tradeNo = request.getParameter("trade_no"); // 支付宝订单号
            //还可以从request中获取更多有用的参数，自己尝试
            boolean notify = alipayService.notify(tradeStatus, outTradeNo, tradeNo);
            if (notify) {
                return "category";
            }
        }
        return "vip";
    }

    /**
     * 支付异步通知
     * 接收到异步通知并验签通过后，一定要检查通知内容，
     * 包括通知中的app_id、out_trade_no、total_amount是否与请求中的一致，并根据trade_status进行后续业务处理。
     * https://docs.open.alipay.com/194/103296
     */
    @RequestMapping("/notify")
    public String notify(HttpServletRequest request) {
        logger.info("alipay notify method success");
        // 验证签名
        boolean flag = alipayService.rsaCheckV1(request);
        if (flag) {
            String tradeStatus = request.getParameter("trade_status"); // 交易状态
            String outTradeNo = request.getParameter("out_trade_no"); // 商户订单号
            logger.info("alipay notify method success;orderNo={}", outTradeNo);
            String tradeNo = request.getParameter("trade_no"); // 支付宝订单号
            //还可以从request中获取更多有用的参数，自己尝试
            boolean notify = alipayService.notify(tradeStatus, outTradeNo, tradeNo);
            if (notify) {
                return "category";
            }
        }
        return "vip";
    }

    /**
     * 退款
     *
     * @param orderNo      订单号
     * @param amount       退款金额
     * @param refundReason 退款原因
     * @return
     */
    @ResponseBody
    @PostMapping("/refund")
    public ResultVO refund(@RequestParam String orderNo, @RequestParam double amount, @RequestParam(required = false) String refundReason) {
        logger.info("alipay refund method success;orderNo={}", orderNo);
        return alipayService.refund(orderNo, amount, refundReason);
    }
}
