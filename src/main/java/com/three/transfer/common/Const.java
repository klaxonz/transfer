package com.three.transfer.common;

import com.three.transfer.util.CapacityUtil;
import com.three.transfer.util.PropertiesUtil;

import java.util.Date;

public class Const {

    public static final String CURRENT_USER = "currentUser";
    private static final String GENERAL_USER_CAPACIRY = PropertiesUtil.getProperty("general.user.total.size");

    public interface CAPACITY {
        Long NORMAL_CAPACITY = CapacityUtil.fileSizeStrToByteNum( GENERAL_USER_CAPACIRY == null ? CapacityUtil.DEFAULT_CAPACITY : GENERAL_USER_CAPACIRY);
    }

    public interface File {
        Long FILE_VAILD_TIME = 7 * 24 * 3600 * 1000L;
    }

    public interface Link{
        Long LINK_VALID_TIME = 24 * 3600 * 1000L;
    }


}
