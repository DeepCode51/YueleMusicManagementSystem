package com.icss.sys.base.module.dict.entity;

import com.icss.sys.base.module.extend.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.icss.sys.base.annotation.ExcelField;

import java.util.ArrayList;
import java.util.List;

/**
 *【字典信息】实体对象
 */

@TableName("sys_dict")
public class SysDict extends BaseEntity {
    /*** 主键id */
    private String id;
    /*** 父级编号 */
    private String pid;
    /*** 标签名 */
    private String label;
    /*** 数据值 */
    private String value;
    /*** 类型 */
    private String type;
    /*** 样式 */
    private String style;
    /*** 排序 */
    private Integer sort;
    /*** 备注信息 */
    private String remarks;
    //初始化避免传值null判断
    @TableField(exist=false)
    private List<SysDict> children = new ArrayList<>();
    @ExcelField(title="编号",align=1, sort=0)
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    @ExcelField(title="父级编号",align=1, sort=1)
    public String getPid() {
        return this.pid;
    }
    public void setPid(String pid) {
        this.pid = pid;
    }
    @ExcelField(title="标签名",align=1, sort=2)
    public String getLabel() {
        return this.label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    @ExcelField(title="数据值",align=1, sort=3)
    public String getValue() {
        return this.value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    @ExcelField(title="类型",align=1, sort=4)
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    @ExcelField(title="样式",align=1, sort=5)
    public String getStyle() {
        return this.style;
    }
    public void setStyle(String style) {
        this.style = style;
    }
    @ExcelField(title="排序",align=1, sort=6)
    public Integer getSort() {
        return sort;
    }
    public void setSort(Integer sort) {
        this.sort = sort;
    }
    @ExcelField(title="备注信息",align=1, sort=7)
    public String getRemarks() {
        return this.remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<SysDict> getChildren() {
        return children;
    }

    public void setChildren(List<SysDict> children) {
        this.children = children;
    }
}