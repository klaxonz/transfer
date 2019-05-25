package com.three.transfer.util;

import java.util.Random;

public class ShareLinkUtil {

    private static String SHARE_LINK_PREFIX = PropertiesUtil.getProperty("share.link.prefix");
    private static final String linkSeed ="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";


    /**
     * 生成随机分享链接后缀
     * @param length    产生字符串的长度
     * @return
     */
    private static String getRandomString(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(linkSeed.charAt(number));
        }
        return sb.toString();
    }

    public static String generateShareLink() {
        String linkAddr = getRandomString(24);
        return generateShareLink(linkAddr);
    }

    private static String generateShareLink(String linkAddr) {
        return SHARE_LINK_PREFIX + linkAddr;
    }


}
