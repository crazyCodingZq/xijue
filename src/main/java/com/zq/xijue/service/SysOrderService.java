package com.zq.xijue.service;

import com.zq.xijue.entity.SysOrder;
import com.zq.xijue.mapper.SysOrderMapper;
import com.zq.xijue.util.SnowflakeIdFactory;
import com.zq.xijue.util.SysConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author: zhangQ
 * @Date: 2020/5/11 22:29
 */
@Service("sysOrderService")
public class SysOrderService {
    private final Logger logger = LoggerFactory.getLogger(SysOrderService.class);
    @Autowired
    private SysOrderMapper sysOrderMapper;

    private final static String PAY_TYPE_ALIPAY = "AliPay";
    private final static String PAY_TYPE_WEPAY = "WePay";

    public String createOrder(String uid, String payType, SysConfig.Vip vipType) throws Exception {
        SysOrder order = new SysOrder();
        order.setUid(uid);
        SnowflakeIdFactory idWorker = new SnowflakeIdFactory(1, 2);
        String orderCode = String.valueOf(idWorker.nextId());
        order.setOrderCode(orderCode);
        order.setCreateDate(new Date());
        order.setOrderDesc(vipType.getOrderDesc());
        order.setOrderType(vipType.getOrderType());

        if (PAY_TYPE_ALIPAY.equals(payType)) {
            order.setPayType(0);
        } else if (PAY_TYPE_WEPAY.equals(payType)) {
            order.setPayType(1);
        }

        order.setOrderPrice(vipType.getAmount());
        // 默认未支付0
        order.setStatus(0);
        sysOrderMapper.insertSelective(order);
        return orderCode;
    }

    /**
     * 查询订单详情
     *
     * @param orderCode
     * @return
     */
    public SysOrder queryOrderDetail(String orderCode) {
        return sysOrderMapper.queryByOrderCode(orderCode);
    }

    /**
     * 更新订单状态
     *
     * @param orderCode
     * @param traceCode
     */
    public void updatePaymentState(String orderCode, String traceCode) {
        sysOrderMapper.updatePaymentState(orderCode, traceCode);
    }
}
