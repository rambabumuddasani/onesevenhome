package com.salesmanager.shop.store.controller.customer;


public class ForgotPwdRequest {
	

	public ForgotPwdRequest() {
		
	}
	private String email;
	private String forgotPwdURL;
	private String ofid;
	private String newPassword;
	private String confirmPassword;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getForgotPwdURL() {
		return forgotPwdURL;
	}
	public void setForgotPwdURL(String forgotPwdURL) {
		this.forgotPwdURL = forgotPwdURL;
	}
	public String getOfid() {
		return ofid;
	}
	public void setOfid(String ofid) {
		this.ofid = ofid;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

}
