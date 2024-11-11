package com.icss.sys.base.module.dict.entity;

import java.util.List;

public class DictTree {

	private String id;
	private String pId;
	private String name;
	private String code;
	private List<DictTree> children;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<DictTree> getChildren() {
		return children;
	}

	public void setChildren(List<DictTree> children) {
		this.children = children;
	}
}

