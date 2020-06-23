package com.zq.xijue.controller;

import com.zq.xijue.core.ResultVO;
import com.zq.xijue.entity.SysOrder;
import com.zq.xijue.service.SysOrderService;
import com.zq.xijue.service.WxPayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 微信支付接口管理
 *
 * @Author: zhangQ
 * @Date: 2020/5/6 23:04
 */
@RestController
@RequestMapping("/wxPay")
public class WxPayController {
    private final Logger logger = LoggerFactory.getLogger(WxPayController.class);
    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private SysOrderService sysOrderService;

    /**
     * 统一下单接口，返回支付二维码
     *
     * @param orderCode
     * @param vipType
     * @param request
     * @return
     */
    @RequestMapping(value = "/unifiedOrder", method = RequestMethod.POST)
    public ResultVO unifiedOrder(String orderCode, String vipType, HttpServletRequest request) {
        try {
            // 1、验证订单是否存在
            SysOrder sysOrder = sysOrderService.queryOrderDetail(orderCode);
            if (sysOrder == null) {
                return new ResultVO(false, "订单不存在");
            }
            // 2、开始微信支付统一下单
            ResultVO resultVO = wxPayService.unifiedOrder(orderCode, sysOrder.getOrderPrice(), sysOrder.getOrderDesc(), request);
            return resultVO;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResultVO(false, "运行异常，请联系管理员");
        }
    }

    /**
     * 微信支付异步通知
     */
    @RequestMapping(value = "/notify")
    public String payNotify(HttpServletRequest request) {
        logger.info("wepay notify method success");
        InputStream is = null;
        String xmlBack = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[报文为空]]></return_msg></xml> ";
        try {
            is = request.getInputStream();
            // 将InputStream转换成String
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            xmlBack = wxPayService.notify(sb.toString());
        } catch (Exception e) {
            logger.error("微信手机支付回调通知失败：", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return xmlBack;
    }

    /**
     * 查询订单支付状态
     *
     * @param orderNo
     * @return
     */
    @RequestMapping(value = "/queryWePayOrder", method = RequestMethod.POST)
    public ResultVO queryWePayOrder(String orderNo) {
        try {
            ResultVO resultVO = wxPayService.queryWePayOrder(orderNo);
            return resultVO;
        } catch (Exception e) {
            logger.error("订单号：{}，错误信息：{}", orderNo, e.getMessage());
            return new ResultVO(false, "微信支付查询订单失败");
        }
    }

    /**
     * 退款
     *
     * @param orderNo      订单号
     * @param amount       退款金额
     * @param refundReason 退款原因
     * @return
     */
    @PostMapping("/refund")
    public ResultVO refund(@RequestParam String orderNo,
                           @RequestParam double amount,
                           @RequestParam(required = false) String refundReason) {
        logger.info("wepay refund method success");
        try {
            return wxPayService.refund(orderNo, amount, refundReason);
        } catch (Exception e) {
            logger.error("退款异常：", e);
        }
        return new ResultVO(false, "退款失败");
    }

}
