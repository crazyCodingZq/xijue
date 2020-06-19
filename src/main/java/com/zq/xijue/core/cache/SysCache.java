package com.zq.xijue.core.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: zhangQ
 * @Date: 2020/5/4 21:27
 */
public class SysCache {
    public static Cache<String, String> mobileCodeCache = CacheBuilder.newBuilder().maximumSize(20000).expireAfterAccess(70, TimeUnit.SECONDS).build();

    private static Map<String, Integer> loginTimes = new HashMap<>();

    public static void put(String key) {
        if (loginTimes.containsKey(key)) {
            loginTimes.put(key, loginTimes.get(key) + 1);
        } else {
            loginTimes.put(key, 1);
        }
    }

    public static int get(String key) {
        if (loginTimes.containsKey(key)) {
            return loginTimes.get(key);
        } else {
            return 0;
        }
    }

    public static void remove(String key) {
        loginTimes.remove(key);
    }
}
