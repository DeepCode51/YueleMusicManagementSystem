package com.icss.sys.utils.gen;

import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * 生成指定位数随机数
 */
public class GenUtils {
    public static String getRuleNo(String prefix,int length){
        int rs = (int) ((Math.random() * 9 + 1) * Math.pow(10, length - 1));
        return prefix+rs;
    }

    public static String getNickName(int len) {
        String ret = "";
        for (int i = 0; i < len; i++) {
            String str = null;
            int hightPos, lowPos; // 定义高低位
            Random random = new Random();
            hightPos = (176 + Math.abs(random.nextInt(39))); //获取高位值
            lowPos = (161 + Math.abs(random.nextInt(93))); //获取低位值
            byte[] b = new byte[2];
            b[0] = (new Integer(hightPos).byteValue());
            b[1] = (new Integer(lowPos).byteValue());
            try {
                str = new String(b, "GBk"); //转成中文
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
            ret += str;
        }
        return ret;
    }
}
