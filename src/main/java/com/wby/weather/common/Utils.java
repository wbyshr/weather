package com.wby.weather.common;

import com.alibaba.fastjson.JSON;

public class Utils {

    public static boolean isJSONStr(String str) {
        boolean result = false;
        try {
            Object obj = JSON.parse(str);
            result = true;
        } catch (Exception e) {
            result = false;
        }
        return result;
    }
}
