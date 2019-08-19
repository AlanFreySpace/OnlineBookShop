package com.you.a.entity.admin;

import org.springframework.stereotype.Component;

@Component
public class Authority {
	private Long id;
	private Long roleId;//½ÇÉ«id
	private Long menuId;//²Ëµ¥id
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public Long getMenuId() {
		return menuId;
	}
	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}
	
}
