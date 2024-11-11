package com.icss.sys.base.enums;

public enum SystemType {
    ADMIN("0"),//管理端
    APPLET("1"),//小程序
    OFFICIAL("2"),//公众号
    FRONT("3");//网站端
    private String value;

    private SystemType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public boolean isEqual(String value) {
        if (value == this.value) {
            return true;
        }
        return false;
    }
}
