package com.three.transfer.util;

import org.apache.commons.lang3.StringUtils;

public class CapacityUtil {

    private static final String KB_UNIT = "KB";
    private static final String MB_UNIT = "MB";
    private static final String GB_UNIT = "GB";
    public static final String DEFAULT_CAPACITY = "20GB";


    public static String fileSizeToStr(Long size) {
        size /= 1024;
        if (size < 1024) {
            return size + "KB";
        }
        size /= 1024;
        if (size < 1024) {
            return size + "MB";
        } else {
            size /= 1024;
            return size + "GB";
        }
    }


    public static Long fileSizeStrToByteNum(String fileSizeStr) {
        String size = fileSizeStr.substring(0, fileSizeStr.length() - 2);
        String sizeUnit = fileSizeStr.substring(fileSizeStr.length() - 2);
        long result = Long.parseLong(size);
        if (StringUtils.equals(sizeUnit.toUpperCase(), KB_UNIT)) {
            result <<= 10;
        }
        if (StringUtils.equals(sizeUnit.toUpperCase(), MB_UNIT)) {
            result <<= 20;
        }
        if (StringUtils.equals(sizeUnit.toUpperCase(), GB_UNIT)) {
            result <<= 30;
        }
        return result;
    }

}
