package com.icss.sys.base.module.gen.service;

import com.icss.sys.base.properteis.GenProperteis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenPathService {
    @Autowired
    private GenProperteis genProperteis;

    public static String firstCharacterToUpper(String srcStr) {
        return srcStr.substring(0, 1).toUpperCase() + srcStr.substring(1);
    }
    public static String firstCharacterToLower(String srcStr) {
        return srcStr.substring(0, 1).toLowerCase() + srcStr.substring(1);
    }

    public static String getBasePathDir() {
        String dir = System.getProperty("user.dir");
        String[] array = dir.split("\\\\");
        String basePath = dir.replace(array[array.length-1], "");
        return basePath;
    }

    public String getBootBasePathDir() {
        String bootBasePath = getBasePathDir() + genProperteis.getBootBasePath();
        return bootBasePath;
    }

    public String getWebPageBasePathDir() {
        String webPageBasePath = getBasePathDir() + genProperteis.getWebPageBasePath();
        return webPageBasePath;
    }
    public String getWxPageBasePathDir() {
        String webPageBasePath = getBasePathDir() + genProperteis.getWxPageBasePath();
        return webPageBasePath;
    }

    public String getBootPathDir() {
        String bootPath = getBootBasePathDir() + genProperteis.getBootPath();
        return bootPath;
    }

    public String getWxApiPathDir() {
        String wxApiPath = getBootBasePathDir() + genProperteis.getWxApiPath();
        return wxApiPath;
    }

    public String getWebPagePathDir() {
        String webPagePath = getWebPageBasePathDir() + genProperteis.getWebPagePath();
        return webPagePath;
    }
    public String getWxPagePathDir() {
        String wxPagePath = getWxPageBasePathDir() + genProperteis.getWxPagePath();
        return wxPagePath;
    }
//    gen.bootPath=${gen.bootBasePath}/src/main/java/com/portal/module
//    gen.wxApiPath=${gen.bootBasePath}/src/main/java/com/api
//    gen.webPagePath=${gen.webPageBasePath}/src/pages/module
//    gen.wxPagePath=${gen.wxPageBasePath}/src/pages/module

}
