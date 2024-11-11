package com.icss.sys.base.module.userRole.entity;

import com.baomidou.mybatisplus.annotation.TableName;

/**
*【用户-角色】实体对象
*/

@TableName("sys_user_role")
public class SysUserRole{
    /*** 主键 */
    private String id;
    /*** 用户编号 */
    private String userId;
    /*** 角色编号 */
    private String roleId;

    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getRoleId() {
        return this.roleId;
    }
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}