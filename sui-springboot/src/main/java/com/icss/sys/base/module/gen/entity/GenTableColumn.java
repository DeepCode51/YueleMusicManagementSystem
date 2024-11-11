package com.icss.sys.base.module.gen.entity;


import com.icss.sys.base.module.extend.entity.BaseEntity;

import static com.icss.sys.base.module.gen.service.GenCreator.getColumnFileName;

/**
 * 业务信息字段管理
 */
public class GenTableColumn extends BaseEntity {
    /*** 编号 */
    private String id;
    /*** 表名称 */
    private String tableName;
    /*** 表描述(中文名称) */
    private String tableDesc;
    /*** 列名称 stu_name*/
    private String columnName;
    /*** 列名称 */
    private String columnFieldName;
    /*** 列名说明 */
    private String columnDesc;
    /*** 数据类型 */
    private String columnDateType;
    /*** 字段类型 */
    private String columnFieldType;
    /*** 控件类型 */
    private String inputType;
    /*** 查询方式（等于、不等于、大于、小于、范围、左LIKE、右LIKE、左右LIKE） */
    private String queryType;
    /*** 字典类型 */
    private String dictType;
    /*** 联想类型 */
    private String associateType;
    /*** 是否隐藏 */
    private int isHidden;
    /*** 是否唯一 */
    private int isUnique;
    /*** 是否主键 */
    private int isPk;
    /*** 是否显示 */
    private int isShow;
    /*** 排序（升序） */
    private int sort;
    /*** 备注信息 */
    private String remarks;

    public String getColumnDateType() {
        return columnDateType;
    }

    public void setColumnDateType(String columnDateType) {
        this.columnDateType = columnDateType;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableDesc() {
        return this.tableDesc;
    }

    public void setTableDesc(String tableDesc) {
        this.tableDesc = tableDesc;
    }

    public String getColumnName() {
        return this.columnName;
    }

    public String getColumnFieldType() {
        return columnFieldType;
    }

    public void setColumnFieldType(String columnFieldType) {
        this.columnFieldType = columnFieldType;
    }

    public void setColumnName(String columnName) {
        this.setColumnFieldName(getColumnFileName(columnName));
        this.columnName = columnName;
    }

    public String getColumnFieldName() {
        return columnFieldName;
    }

    public void setColumnFieldName(String columnFieldName) {
        this.columnFieldName = columnFieldName;
    }

    public String getColumnDesc() {
        return columnDesc;
    }

    public void setColumnDesc(String columnDesc) {
        this.columnDesc = columnDesc;
    }

    public String getInputType() {
        return this.inputType;
    }

    public void setInputType(String inputType) {
        String columnFieldType="String";
        if(inputType.equals("date")){
            columnFieldType="Date";
        }else if(inputType.equals("select")){
            columnFieldType="Integer";
        }
        this.setColumnFieldType(columnFieldType);
        this.inputType = inputType;
    }

    public String getQueryType() {
        return this.queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public String getDictType() {
        return this.dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    public String getAssociateType() {
        return associateType;
    }

    public void setAssociateType(String associateType) {
        this.associateType = associateType;
    }

    public int getIsHidden() {
        return isHidden;
    }

    public void setIsHidden(int isHidden) {
        this.isHidden = isHidden;
    }

    public int getIsUnique() {
        return isUnique;
    }

    public void setIsUnique(int isUnique) {
        this.isUnique = isUnique;
    }

    public int getIsPk() {
        return isPk;
    }

    public void setIsPk(int isPk) {
        this.isPk = isPk;
    }

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}