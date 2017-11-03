package com.salesmanager.shop.admin.controller;

public class DeleteSubCatImgResponse {

	private String successMessage;
	private String errorMesssage;
	private String status;
	public String getSuccessMessage() {
		return successMessage;
	}
	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}
	public String getErrorMesssage() {
		return errorMesssage;
	}
	public void setErrorMesssage(String errorMesssage) {
		this.errorMesssage = errorMesssage;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
