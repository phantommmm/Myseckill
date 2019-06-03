package com.phantom.seckill.utils;

public class ValidationUtil {
    public static boolean isPhone(String str) {
        String regex = "^((13[0-9])|(14[579])|(15([0-3]|[5-9]))|(166)|(17[0135678])|(18[0-9])|(19[8|9]))\\d{8}$";
        if (str == null || str.length() != 11) {
            return false;
        }
        return str.matches(regex);
    }
}
