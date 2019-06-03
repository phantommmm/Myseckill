package com.phantom.seckill.utils;



public class RedisKeyUtil {

    private static final String SPLIT = ":";

    public static String accessKey(Long userId, String url) {
        return "ACCESS:" + url + SPLIT + userId;
    }

    public static String stockKey(Long goodsId) {
        return "STOCK:" + goodsId;
    }

    public static String tokenKey(String token) {
        return "TOKEN:" + token;
    }

    public static String codeKey(Long id,Long goodsId){return id+SPLIT+goodsId+SPLIT+"CODE:";}

    public static String MiusaPathKey(String path, Long id) {
        System.out.println("MIUSA_PATH:" + id + SPLIT + path);
        return "MIUSA_PATH:" + id + SPLIT + path;
    }

    public static String miusaOrderKey(Long userId, Long goodsId) {
        return "MIUSA_ORDER:" + goodsId + SPLIT + userId;
    }

    public static String miusaFailedKey(Long goodsId) {
        return "MIUSA_Failed:"+goodsId;
    }


}
