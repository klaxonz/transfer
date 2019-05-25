package com.three.transfer.common;

public enum ResponseCode {
    SUCCESS(0, "SUCCESS"),
    ERROR(1, "ERROR"),
    ILLEAGEL_ARGUMENT(5, "ILLEAGEL_ARGUMENT"),
    NEED_LOGIN(10, "NEED_LOGIN"),
    FILE_UPLOAD_SUCCESS(15, "FILE_UPLOAD_SUCCESS");



    private int code;
    private String msg;

    ResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
