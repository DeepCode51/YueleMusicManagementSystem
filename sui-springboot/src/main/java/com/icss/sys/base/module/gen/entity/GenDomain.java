package com.icss.sys.base.module.gen.entity;

/**
 * Created by Administrator on 2018/7/23.
 */
public class GenDomain {

    private String path;
    private String tableName;
    private String prefixPath;
    private String isFileList;
    private String isPort;
    private String modelName;
    private String comments;

    public String getIsFileList() {
        return isFileList;
    }

    public void setIsFileList(String isFileList) {
        this.isFileList = isFileList;
    }

    public String getIsPort() {
        return isPort;
    }

    public void setIsPort(String isPort) {
        this.isPort = isPort;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getPrefixPath() {
        return prefixPath;
    }

    public void setPrefixPath(String prefixPath) {
        this.prefixPath = prefixPath;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
