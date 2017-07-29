package com.salesmanager.shop.store.controller.customer;

import java.io.Serializable;

public class LoginResponse implements Serializable {
	
private static final long serialVersionUID = 1L;
	
	private boolean success;
	private Long userId;
	private  String errorMessage;
	private String type;
	private String name;

	public String getErrorMessage() {
		return errorMessage;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public void setErrorMessage(String string) {
		this.errorMessage = string;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
