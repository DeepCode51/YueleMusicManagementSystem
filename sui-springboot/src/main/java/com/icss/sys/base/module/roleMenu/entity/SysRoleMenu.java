package com.icss.sys.base.module.roleMenu.entity;

import com.baomidou.mybatisplus.annotation.TableName;

/**
*【角色-菜单】实体对象
*/

@TableName("sys_role_menu")
public class SysRoleMenu{
    /*** 主键 */
    private String id;
    /*** 角色编号 */
    private String roleId;
    /*** 菜单编号 */
    private String menuId;

    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getRoleId() {
        return this.roleId;
    }
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
    public String getMenuId() {
        return this.menuId;
    }
    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
}