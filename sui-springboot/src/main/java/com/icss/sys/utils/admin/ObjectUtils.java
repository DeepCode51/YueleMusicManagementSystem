package com.icss.sys.utils.admin;

public class ObjectUtils extends org.apache.commons.lang3.ObjectUtils {
    //判断对象是否不为空
    public static boolean isNotNull(Object obj) {
        return obj != null;
    }
    //判断对象是否为空
    public static boolean isNull(Object obj) {
        return obj == null;
    }
}
