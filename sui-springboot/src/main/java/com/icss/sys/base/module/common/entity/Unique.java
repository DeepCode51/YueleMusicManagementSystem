package com.icss.sys.base.module.common.entity;

import java.util.Map;

/**
 * Created by zenglf on 2019/11/9.
 */
public class Unique {
    private String tableName;
    private Map<String ,Object> fields;
    private String id;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Map<String, Object> getFields() {
        return fields;
    }

    public void setFields(Map<String, Object> fields) {
        this.fields = fields;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
