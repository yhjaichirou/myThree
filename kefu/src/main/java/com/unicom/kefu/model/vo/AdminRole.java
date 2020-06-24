package com.unicom.kefu.model.vo;

import java.util.List;

public class AdminRole {

	private Integer id;
	private String roleName;
	private String rolePrimary;
	private String roleDescribe;
	private Integer status;
	
	
	//外键
	public List<AdminNav> menus;
	private Integer menuId;
	
	public List<AdminNav> getMenus() {
		return menus;
	}
	public void setMenus(List<AdminNav> menus) {
		this.menus = menus;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getRolePrimary() {
		return rolePrimary;
	}
	public void setRolePrimary(String rolePrimary) {
		this.rolePrimary = rolePrimary;
	}
	public String getRoleDescribe() {
		return roleDescribe;
	}
	public void setRoleDescribe(String roleDescribe) {
		this.roleDescribe = roleDescribe;
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



	

}