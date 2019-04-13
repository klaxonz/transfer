package com.three.transfer.util;

import java.util.Random;

public class ShareLinkUtil {

    private static String SHARE_LINK_PREFIX = "localhost:8080/transfer/share/";


    /**
     * 生成随机分享链接后缀
     * @param length 产生字符串的长度
     * @return
     */
    public static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static String generateShareLink(String linkAddr) {
        return SHARE_LINK_PREFIX + linkAddr;
    }


}
