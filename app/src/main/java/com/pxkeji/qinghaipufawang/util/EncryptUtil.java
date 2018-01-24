package com.pxkeji.qinghaipufawang.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/20.
 */

public class EncryptUtil {

    public static String Md5Data(LinkedHashMap map) {
        StringBuilder sb = new StringBuilder();
        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            sb.append(entry.getKey().toString());
            sb.append(entry.getValue().toString());
        }
        sb.append("qfsdfsdfasd");
        return Md5(sb.toString());
    }

    public static String Md5(String plainText) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(plainText.getBytes("utf-8"));
        } catch (Exception e) {
            throw new RuntimeException("没有md5这个算法！");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);
        int num = md5code.length();
        int mn = 32-num;
        String w="";
        for(int i=0;i<mn;i++){
            w+=0;
        }
        String aa = w+md5code;
        return aa;
    }

}
