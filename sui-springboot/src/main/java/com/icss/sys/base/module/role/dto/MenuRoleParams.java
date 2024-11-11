package com.icss.sys.base.module.role.dto;

import java.util.List;

public class MenuRoleParams {

	private String roleId;

	private List<String> menuIds;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public List<String> getMenuIds() {
		return menuIds;
	}

	public void setMenuIds(List<String> menuIds) {
		this.menuIds = menuIds;
	}
}

