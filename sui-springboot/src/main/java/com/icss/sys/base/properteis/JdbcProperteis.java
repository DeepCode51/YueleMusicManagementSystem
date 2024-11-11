package com.icss.sys.base.properteis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 文件上传相关属性
 * @create 2018-04-22 13:58
 **/
@Component
@ConfigurationProperties(prefix = "spring.datasource")
public class JdbcProperteis {
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}