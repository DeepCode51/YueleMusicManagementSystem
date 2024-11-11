package com.icss.sys.base.module.menu.entity;

import com.icss.sys.base.module.extend.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.icss.sys.base.annotation.ExcelField;

import java.util.ArrayList;
import java.util.List;

/**
*【菜单信息】实体对象
*/

@TableName("sys_menu")
public class SysMenu extends BaseEntity{
    //菜单id
    private String id;
    //父级id
    private String pid;
    //菜单名称
    private String name;
    //菜单排序
    private String sort;
    //菜单链接
    private String href;
    //菜单组键
    private String component;
    //菜单图标
    private String icon;
    //是否显示
    private Integer isShow;
    //菜单权限
    private String permission;
    //备注
    private String remarks;
    //初始化避免传值null判断
    @TableField(exist=false)
    private List<SysMenu> children = new ArrayList<>();

    @ExcelField(title="主键id",align=1, sort=0)
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    @ExcelField(title="名称",align=1, sort=1)
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @ExcelField(title="主键",align=1, sort=2)
    public String getComponent() {
        return this.component;
    }
    public void setComponent(String component) {
        this.component = component;
    }
    @ExcelField(title="是否在菜单中显示",dictType="sys_yes_no",align=1, sort=3)
    public Integer getIsShow() {
        return this.isShow;
    }
    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }
    @ExcelField(title="链接",align=1, sort=4)
    public String getHref() {
        return this.href;
    }
    public void setHref(String href) {
        this.href = href;
    }
    @ExcelField(title="权限标识",align=1, sort=5)
    public String getPermission() {
        return this.permission;
    }
    public void setPermission(String permission) {
        this.permission = permission;
    }
    @ExcelField(title="排序",align=1, sort=6)
    public String getSort() {
        return this.sort;
    }
    public void setSort(String sort) {
        this.sort = sort;
    }
    @ExcelField(title="父级编号",align=1, sort=7)
    public String getPid() {
        return this.pid;
    }
    public void setPid(String pid) {
        this.pid = pid;
    }
    @ExcelField(title="图标",align=1, sort=8)
    public String getIcon() {
        return this.icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    @ExcelField(title="备注信息",align=1, sort=9)
    public String getRemarks() {
        return this.remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<SysMenu> getChildren() {
        return children;
    }

    public void setChildren(List<SysMenu> children) {
        this.children = children;
    }
}