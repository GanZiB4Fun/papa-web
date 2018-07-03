package com.ganzib.papa.app.utils;

public class RespBase {

    public static final int SUCCUSS = 1000;

    public static final int FAIL = 9999;
    public static final String DESC_SUCCUSS = "成功";

    public static final String DESC_FAIL = "失败";

    private Integer code = SUCCUSS;

    private String msg = DESC_SUCCUSS;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
