package com.zq.xijue.util;

import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @Author: zhangQ
 * @Date: 2020/5/31 15:33
 */
public class UserUtils {
    public static String getLoginName() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if ("anonymousUser".equals(username)) {
            return "游客";
        }
        return username;
    }
}
