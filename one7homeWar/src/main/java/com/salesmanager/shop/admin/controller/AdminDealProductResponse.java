package com.salesmanager.shop.admin.controller;

import java.util.Date;

public class AdminDealProductResponse {

	private Long productId;
	private String productName;
	private Date productPriceSpecialEndDate;
	private Date productPriceSpecialStartDate;
	private String errorMesg;
	public String getErrorMesg() {
		return errorMesg;
	}
	public void setErrorMesg(String errorMesg) {
		this.errorMesg = errorMesg;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Date getProductPriceSpecialEndDate() {
		return productPriceSpecialEndDate;
	}
	public void setProductPriceSpecialEndDate(Date productPriceSpecialEndDate) {
		this.productPriceSpecialEndDate = productPriceSpecialEndDate;
	}
	public Date getProductPriceSpecialStartDate() {
		return productPriceSpecialStartDate;
	}
	public void setProductPriceSpecialStartDate(Date productPriceSpecialStartDate) {
		this.productPriceSpecialStartDate = productPriceSpecialStartDate;
	}
	
}
