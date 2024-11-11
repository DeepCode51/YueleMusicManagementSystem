package com.icss.sys.base.properteis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 文件上传相关属性
 * @create 2018-04-22 13:58
 **/
@Component
@ConfigurationProperties(prefix = "upload")
public class FileUploadProperteis {

    //静态资源对外暴露的访问路径
    private String staticAccessPath;
    //静态资源对外暴露的访问路径
    private String diskPath;

    //文件上传目录
    private String fileBasePath ;

    public String getStaticAccessPath() {
        return staticAccessPath;
    }

    public void setStaticAccessPath(String staticAccessPath) {
        this.staticAccessPath = staticAccessPath;
    }

    public String getDiskPath() {
        return diskPath;
    }

    public void setDiskPath(String diskPath) {
        this.diskPath = diskPath;
    }

    public String getFileBasePath() {
        return fileBasePath;
    }

    public void setFileBasePath(String fileBasePath) {
        this.fileBasePath = fileBasePath;
    }
}