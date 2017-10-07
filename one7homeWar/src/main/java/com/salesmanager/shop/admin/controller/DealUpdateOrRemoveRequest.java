package com.salesmanager.shop.admin.controller;

import java.util.Date;

public class DealUpdateOrRemoveRequest {

	private Long productId;
	private Date productPriceSpecialEndDate;
	private Date productPriceSpecialStartDate;
	private boolean status;
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
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
