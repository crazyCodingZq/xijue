package com.zq.xijue.controller;

import com.zq.xijue.constant.OrderConstant;
import com.zq.xijue.core.ResultVO;
import com.zq.xijue.entity.SysOrder;
import com.zq.xijue.service.SysOrderService;
import com.zq.xijue.util.SysConfig;
import com.zq.xijue.util.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: zhangQ
 * @Date: 2020/5/11 22:19
 */
@Controller
@RequestMapping("/order")
public class SysOrderController {
    private final Logger logger = LoggerFactory.getLogger(SysOrderService.class);
    @Autowired
    private SysOrderService sysOrderService;

    /**
     * 创建订单，返回订单号
     *
     * @param payType
     * @param amount
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/createOrder", method = RequestMethod.POST)
    public ResultVO createOrder(String payType, String amount) {
        SysConfig.Vip vipType = OrderConstant.vipMap.get(amount);
        if (StringUtils.isEmpty(payType) || StringUtils.isEmpty(amount) || vipType == null) {
            return new ResultVO(false, "参数错误,请重试");
        }
        try {
            String username = UserUtils.getLoginName();
            String orderCode = sysOrderService.createOrder(username, payType, vipType);
            return new ResultVO(orderCode);
        } catch (Exception e) {
            logger.error("create order fail,Exception:", e);
            return new ResultVO(false, "");
        }
    }

    /**
     * 查询订单状态
     *
     * @param orderNo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryOrderStatus", method = RequestMethod.POST)
    public ResultVO queryOrderStatus(String orderNo) {
        try {
            SysOrder sysOrder = sysOrderService.queryOrderDetail(orderNo);
            if (sysOrder.getStatus() == 1) {
                return new ResultVO(true, "支付成功");
            } else {
                return new ResultVO(false, "订单待支付");
            }
        } catch (Exception e) {
            logger.error("create order fail,Exception:", e);
            return new ResultVO(false, "");
        }
    }

}
