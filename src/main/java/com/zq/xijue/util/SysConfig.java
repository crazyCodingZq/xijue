package com.zq.xijue.util;

import com.zq.xijue.constant.OrderConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Author: zhangQ
 * @Date: 2020/6/1 0:51
 */
@Data
@Component
@ConfigurationProperties(prefix = "sys.config")
public class SysConfig {
    private String vipSeasonPrice;
    private String vipYearPrice;
    private String vipLifePrice;
    private String vipSeasonText;
    private String vipYearText;
    private String vipLifeText;

    @PostConstruct
    public void initVipInfo() {
        OrderConstant.vipMap.put(getVipSeasonPrice(), new Vip(getVipSeasonPrice(), getVipSeasonText(), 0));
        OrderConstant.vipMap.put(getVipYearPrice(), new Vip(getVipYearPrice(), getVipYearText(), 1));
        OrderConstant.vipMap.put(getVipLifePrice(), new Vip(getVipLifePrice(), getVipLifeText(), 2));
    }

    public class Vip {
        private double amount;
        private String orderDesc;
        private int orderType;

        public Vip(String price, String desc, int type) {
            amount = Double.valueOf(price);
            orderDesc = desc;
            orderType = type;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getOrderDesc() {
            return orderDesc;
        }

        public void setOrderDesc(String orderDesc) {
            this.orderDesc = orderDesc;
        }

        public int getOrderType() {
            return orderType;
        }

        public void setOrderType(int orderType) {
            this.orderType = orderType;
        }
    }
}
