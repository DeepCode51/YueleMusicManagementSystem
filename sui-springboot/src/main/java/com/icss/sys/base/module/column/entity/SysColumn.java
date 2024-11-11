package com.icss.sys.base.module.column.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.icss.sys.base.module.extend.entity.BaseEntity;
import com.icss.sys.base.annotation.ExcelField;

/**
*【字段信息】实体对象
*/

@TableName("sys_column")
public class SysColumn extends BaseEntity {
    /*** 主键id */
    private String id;
    /*** 表名称 */
    private String tableName;
    /*** 表描述 */
    private String tableDesc;
    /*** 列名称 */
    private String columnName;
    /*** 列描述 */
    private String columnDesc;
    /*** 控件类型 */
    private String inputType;
    /*** 查询类型 */
    private String queryType;
    /*** 字典类型 */
    private String dictType;
    /*** 关联类型 */
    private String associateType;
    /*** 是否隐藏 */
    private Integer isHidden;
    /*** 是否唯一 */
    private Integer isUnique;
    /*** 是否主键 */
    private Integer isPk;
    /*** 是否显示 */
    private Integer isShow;
    /*** 排序 */
    private String sort;
    /*** 备注信息 */
    private String remarks;
    @ExcelField(title="主键id",align=1, sort=0)
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    @ExcelField(title="表名称",align=1, sort=1)
    public String getTableName() {
        return this.tableName;
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    @ExcelField(title="表描述",align=1, sort=2)
    public String getTableDesc() {
        return this.tableDesc;
    }
    public void setTableDesc(String tableDesc) {
        this.tableDesc = tableDesc;
    }
    @ExcelField(title="列名称",align=1, sort=3)
    public String getColumnName() {
        return this.columnName;
    }
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
    @ExcelField(title="列描述",align=1, sort=4)
    public String getColumnDesc() {
        return this.columnDesc;
    }
    public void setColumnDesc(String columnDesc) {
        this.columnDesc = columnDesc;
    }
    @ExcelField(title="控件类型",align=1, sort=5)
    public String getInputType() {
        return this.inputType;
    }
    public void setInputType(String inputType) {
        this.inputType = inputType;
    }
    @ExcelField(title="查询类型",align=1, sort=6)
    public String getQueryType() {
        return this.queryType;
    }
    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }
    @ExcelField(title="字典类型",align=1, sort=7)
    public String getDictType() {
        return this.dictType;
    }
    public void setDictType(String dictType) {
        this.dictType = dictType;
    }
    public String getAssociateType() {
        return this.associateType;
    }
    public void setAssociateType(String associateType) {
        this.associateType = associateType;
    }
    public Integer getIsHidden() {
        return this.isHidden;
    }
    public void setIsHidden(Integer isHidden) {
        this.isHidden = isHidden;
    }
    public Integer getIsUnique() {
        return this.isUnique;
    }
    public void setIsUnique(Integer isUnique) {
        this.isUnique = isUnique;
    }
    public Integer getIsPk() {
        return this.isPk;
    }
    public void setIsPk(Integer isPk) {
        this.isPk = isPk;
    }
    public Integer getIsShow() {
        return this.isShow;
    }
    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }
    public String getSort() {
        return this.sort;
    }
    public void setSort(String sort) {
        this.sort = sort;
    }
    public String getRemarks() {
        return this.remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}