package com.salesmanager.shop.admin.controller;

import java.io.Serializable;

public class UpdatePasswordReq implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String adminName;
	private String newPassword;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAdminName() {
		return adminName;
	}
	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

}
