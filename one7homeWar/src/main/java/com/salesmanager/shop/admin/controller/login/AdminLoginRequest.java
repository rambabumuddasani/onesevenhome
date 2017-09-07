package com.salesmanager.shop.admin.controller.login;

import java.io.Serializable;

public class AdminLoginRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String userName;
	private String password;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPasword(String password) {
		this.password = password;
	}
	

}
