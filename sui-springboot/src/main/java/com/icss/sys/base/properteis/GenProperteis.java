package com.icss.sys.base.properteis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 文件上传相关属性
 * @create 2018-04-22 13:58
 **/
@Component
@ConfigurationProperties(prefix = "gen")
public class GenProperteis {
    private String bootPath;
    private String wxApiPath;
    private String webPagePath;
    private String wxPagePath;

    private String bootBasePath;
    private String webPageBasePath;
    private String wxPageBasePath;

    public String getBootPath() {
        return bootPath;
    }

    public void setBootPath(String bootPath) {
        this.bootPath = bootPath;
    }

    public String getWxApiPath() {
        return wxApiPath;
    }

    public void setWxApiPath(String wxApiPath) {
        this.wxApiPath = wxApiPath;
    }

    public String getWebPagePath() {
        return webPagePath;
    }

    public void setWebPagePath(String webPagePath) {
        this.webPagePath = webPagePath;
    }

    public String getWxPagePath() {
        return wxPagePath;
    }

    public void setWxPagePath(String wxPagePath) {
        this.wxPagePath = wxPagePath;
    }

    public String getBootBasePath() {
        return bootBasePath;
    }

    public void setBootBasePath(String bootBasePath) {
        this.bootBasePath = bootBasePath;
    }

    public String getWebPageBasePath() {
        return webPageBasePath;
    }

    public void setWebPageBasePath(String webPageBasePath) {
        this.webPageBasePath = webPageBasePath;
    }

    public String getWxPageBasePath() {
        return wxPageBasePath;
    }

    public void setWxPageBasePath(String wxPageBasePath) {
        this.wxPageBasePath = wxPageBasePath;
    }
}