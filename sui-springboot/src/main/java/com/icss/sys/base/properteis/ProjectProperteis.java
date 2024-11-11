package com.icss.sys.base.properteis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 文件上传相关属性
 * @create 2018-04-22 13:58
 **/
@Component
@ConfigurationProperties(prefix = "base")
public class ProjectProperteis {

    //静态资源对外暴露的访问路径
    private String projectName;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}