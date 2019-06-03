package com.phantom.seckill.utils;


import org.apache.commons.codec.digest.DigestUtils;


public class MD5Util {

    public static String encrypt(String msg){
        return DigestUtils.md5Hex(msg);
    }



}
