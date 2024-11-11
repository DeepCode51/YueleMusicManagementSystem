package com.icss.sys.base.module.gen.entity;


import java.util.List;

/**
 * Created by Administrator on 2019/7/23.
 */
public class GenTableConfig {

    private String tableName;
    private String tableDesc;
    private boolean isLock;
    private boolean isPort;
    private boolean isFileList;

    List<GenTableColumn> tableOptions;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableDesc() {
        return tableDesc;
    }

    public void setTableDesc(String tableDesc) {
        this.tableDesc = tableDesc;
    }

    public boolean isLock() {
        return isLock;
    }

    public void setLock(boolean lock) {
        isLock = lock;
    }

    public boolean isPort() {
        return isPort;
    }

    public void setPort(boolean port) {
        isPort = port;
    }

    public boolean isFileList() {
        return isFileList;
    }

    public void setFileList(boolean fileList) {
        isFileList = fileList;
    }

    public List<GenTableColumn> getTableOptions() {
        return tableOptions;
    }

    public void setTableOptions(List<GenTableColumn> tableOptions) {
        this.tableOptions = tableOptions;
    }
}
