package com.salesmanager.shop.admin.controller;

public class SubCatImageResponse {
    
	Long subCategoryId;
	String subCatImgURL;
	String successMessage;
	String errorMessage;
	String status;
	public Long getSubCategoryId() {
		return subCategoryId;
	}
	public void setSubCategoryId(Long subCategoryId) {
		this.subCategoryId = subCategoryId;
	}

	public String getSubCatImgURL() {
		return subCatImgURL;
	}
	public void setSubCatImgURL(String subCatImgURL) {
		this.subCatImgURL = subCatImgURL;
	}
	public String getSuccessMessage() {
		return successMessage;
	}
	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
