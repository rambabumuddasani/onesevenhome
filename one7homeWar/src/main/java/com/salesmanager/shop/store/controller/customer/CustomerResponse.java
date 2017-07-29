package com.salesmanager.shop.store.controller.customer;


public class CustomerResponse {
	

	public CustomerResponse() {
		
	}
	String errorMessage;
	String successMessage;
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getSuccessMessage() {
		return successMessage;
	}
	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}
	
}
