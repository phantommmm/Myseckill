package com.phantom.seckill.utils;

import java.util.Random;
import java.util.UUID;


public class UUIDUtil {

    public static String getSalt() {
        return UUID.randomUUID().toString().substring(0, 5);
    }

    public static String getToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String getRandomPath() {
        Random random = new Random();
        String rdmPath = UUID.randomUUID().toString().replace("-", "");
        return MD5Util.encrypt(rdmPath + random.nextInt(10_000));
    }


}
