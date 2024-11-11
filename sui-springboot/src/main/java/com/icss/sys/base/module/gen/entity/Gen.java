package com.icss.sys.base.module.gen.entity;


import com.icss.sys.base.module.extend.entity.BaseEntity;

//表名:GEN
public class Gen extends BaseEntity {
    private String id;
    private String columnId;//列编号 库名+表名+字段
    private String columnKey; //是否是主键
    private String comments;
    private String tableName;
    private String columnName;
    private String columnType;
    private int columnLength;
    private String columnDescribe;
    private String dbName;

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColumnKey() {
        return columnKey;
    }

    public void setColumnKey(String columnKey) {
        this.columnKey = columnKey;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public int getColumnLength() {
        return columnLength;
    }

    public void setColumnLength(int columnLength) {
        this.columnLength = columnLength;
    }

    public String getColumnDescribe() {
        return columnDescribe;
    }

    public void setColumnDescribe(String columnDescribe) {
        this.columnDescribe = columnDescribe;
    }
}