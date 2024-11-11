package com.icss.sys.base.module.role.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.icss.sys.base.module.extend.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.icss.sys.base.annotation.ExcelField;

/**
*【角色信息】实体对象
*/

@TableName("sys_role")
public class SysRole extends BaseEntity {
    /*** 角色id */
    private String id;
    /*** 备注信息 */
    private String remarks;
    /*** 是否勾选 */
    @TableField(exist = false)
    private boolean checked;
    /*** 角色编码 */
    private String roleCode;
    /*** 角色名称 */
    private String roleName;

    @ExcelField(title="主键id",align=1, sort=0)
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    @ExcelField(title="角色名称",align=1, sort=1)
    public String getRoleName() {
        return this.roleName;
    }
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    @ExcelField(title="角色编码",align=1, sort=2)
    public String getRoleCode() {
        return this.roleCode;
    }
    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }
    @ExcelField(title="备注信息",align=1, sort=3)
    public String getRemarks() {
        return this.remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}