package com.icss.sys.base.module.role.dto;

import java.util.List;

public class MenuRoleTree {

	private String id;
	private String name;
	private String label;//兼容element树形菜单
	private String href;
	private boolean checked;
	private boolean spread=true; //默认展开
	private List<MenuRoleTree> children;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public boolean isSpread() {
		return spread;
	}

	public void setSpread(boolean spread) {
		this.spread = spread;
	}

	public List<MenuRoleTree> getChildren() {
		return children;
	}

	public void setChildren(List<MenuRoleTree> children) {
		this.children = children;
	}
}

