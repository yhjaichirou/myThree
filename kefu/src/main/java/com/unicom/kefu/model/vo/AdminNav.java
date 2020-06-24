package com.unicom.kefu.model.vo;

import java.util.List;

public class AdminNav {
	private Integer id;
	private String name;
	private Integer pid;
	private String url;
	private String icon;
	private Integer sort;
	private Integer status;
	public List<AdminNav> children;
	
	//外键
	private Integer roleId;
	private Integer menuId;
	private String menuName;
	
	//为满足tree
	private String title; 

	public AdminNav() {
	}

	public AdminNav(String name) {
		this.name = name;
	}

	public AdminNav(String name, Integer pid, String url, String icon, String sort) {
		this.name = name;
		this.pid = pid;
		this.url = url;
		this.icon = icon;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPid() {
		return this.pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public List<AdminNav> getChildren() {
		return children;
	}

	public void setChildren(List<AdminNav> children) {
		this.children = children;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String setIcon(String icon) {
		return icon;
	}
	
	public String getIcon() {
		return icon;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

}
